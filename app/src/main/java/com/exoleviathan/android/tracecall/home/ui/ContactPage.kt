package com.exoleviathan.android.tracecall.home.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.exoleviathan.android.tracecall.R
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryDark
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryLight
import com.exoleviathan.android.tracecall.home.model.ContactInfo
import com.exoleviathan.android.tracecall.home.model.ContactIntents
import com.exoleviathan.android.tracecall.home.model.ContactStates
import com.exoleviathan.android.tracecall.home.viewmodel.ContactViewModel
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@Composable
fun ContactPage(contactViewModel: ContactViewModel, modifier: Modifier) {
    LaunchedEffect(Unit) {
        contactViewModel.contactIntents.send(ContactIntents.FetchAllUniqueContactList)
    }

    var isShowImagePopup by remember { mutableStateOf<Uri?>(null) }
    val contactListState = contactViewModel.contactListState.collectAsState()
    when (contactListState.value) {
        ContactStates.InitialState -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        is ContactStates.ContactListUpdated -> {
            ContactItemsCard(
                contactViewModel,
                modifier,
                (contactListState.value as? ContactStates.ContactListUpdated)?.contactList ?: arrayListOf()
            ) { data ->
                isShowImagePopup = data
            }
        }
    }

    isShowImagePopup?.let {
        ShowImagePopup(it) {
            isShowImagePopup = null
        }
    }
}

@Composable
fun ContactItemsCard(
    contactViewModel: ContactViewModel,
    modifier: Modifier,
    contactList: List<ContactInfo>,
    uriData: (Uri?) -> Unit
) {
    Card(
        modifier = modifier.padding(horizontal = 10.dp).fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        var expandedItemIndex by remember { mutableStateOf<Int?>(null) }
        LazyColumn {
            itemsIndexed(contactList) { index, contactInfo ->
                if (index != 0) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                }

                ContactItem(
                    contactViewModel,
                    contactInfo,
                    (expandedItemIndex == index),
                    onClick = { expandedItemIndex = if (expandedItemIndex == index) null else index },
                    onShowImagePopup = { data -> uriData.invoke(data) }
                )
            }
        }
    }
}

@Composable
fun ContactItem(
    contactViewModel: ContactViewModel,
    contactInfo: ContactInfo,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onShowImagePopup: (data: Uri?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    coroutineScope.launch {
                        contactViewModel.contactIntents.send(ContactIntents.CopyContactInfoToClipboard(contactInfo))
                    }
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            contactInfo.contactPhotoUri?.let {
                val data = it.toUri()
                AsyncImage(
                    model = ImageRequest.Builder(context).data(data).crossfade(true).build(),
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(18.dp)).clickable { onShowImagePopup.invoke(data) },
                    placeholder = painterResource(R.drawable.ic_contact),
                    error = painterResource(R.drawable.ic_contact),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            } ?: run {
                Image(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(18.dp)),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_contact),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimaryLight),
                    contentDescription = null
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = contactInfo.contactName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (isExpanded) {
                    Text(
                        text = contactInfo.contactNumber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ShowImagePopup(data: Uri, onDismissRequest: () -> Unit) {
    val context = LocalContext.current

    Popup(alignment = Alignment.Center, onDismissRequest = onDismissRequest) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(data).crossfade(true).build(),
            modifier = Modifier.size(width = 240.dp, height = 360.dp).clip(RoundedCornerShape(12.dp)),
            placeholder = painterResource(R.drawable.ic_contact),
            error = painterResource(R.drawable.ic_contact),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            alignment = Alignment.Center
        )
    }
}