package com.exoleviathan.android.tracecall.home.ui

import android.net.Uri
import android.text.TextUtils
import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.exoleviathan.android.tracecall.R
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryDark
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryLight
import com.exoleviathan.android.tracecall.home.model.CallLogDetailsInfo
import com.exoleviathan.android.tracecall.home.model.CallLogInfo
import com.exoleviathan.android.tracecall.home.model.CallLogIntents
import com.exoleviathan.android.tracecall.home.model.CallLogStates
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel
import java.util.Date

@Composable
fun CallLogPage(callLogViewModel: CallLogViewModel, modifier: Modifier) {
    LaunchedEffect(Unit) {
        callLogViewModel.callLogIntents.send(CallLogIntents.FetchAllCallLogInfo)
    }

    var isShowImagePopup by remember { mutableStateOf<Uri?>(null) }
    val callLogState = callLogViewModel.callLogStates.collectAsState()

    when (callLogState.value) {
        CallLogStates.InitialState -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        is CallLogStates.CallLogsListUpdated -> {
            CallLogItemsCard(modifier, (callLogState.value as? CallLogStates.CallLogsListUpdated)?.callLogList ?: arrayListOf()) { data ->
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
fun CallLogItemsCard(modifier: Modifier, callLogList: List<CallLogInfo>, uriData: (Uri?) -> Unit) {
    Card(modifier = modifier.padding(horizontal = 10.dp).fillMaxWidth(), shape = RoundedCornerShape(26.dp), elevation = CardDefaults.cardElevation(0.dp)) {
        var expandedItemIndex by remember { mutableStateOf<Int?>(null) }
        LazyColumn {
            itemsIndexed(callLogList) { index, callLogInfo ->
                if (index != 0) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp))
                }

                CallLogItem(
                    callLogInfo,
                    (expandedItemIndex == index),
                    onClick = { expandedItemIndex = if (expandedItemIndex == index) null else index },
                    onShowImagePopup = { data -> uriData.invoke(data) }
                )
            }
        }
    }
}

@Composable
fun CallLogItem(
    callLogInfo: CallLogInfo,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onShowImagePopup: (data: Uri?) -> Unit
) {
    Column(Modifier.fillMaxWidth().combinedClickable(onClick = onClick)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            val context = LocalContext.current
            callLogInfo.contactInfo.contactPhotoUri?.let {
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
                if (!TextUtils.isEmpty(callLogInfo.contactInfo.contactName)) {
                    Text(
                        text = callLogInfo.contactInfo.contactName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = callLogInfo.contactInfo.contactNumber,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (isExpanded) {
            LazyColumn(userScrollEnabled = false, modifier = Modifier.height((callLogInfo.details.size * 60).dp)) {
                items(callLogInfo.details.toList()) { details ->
                    CallLogDetailsPage(details)
                }
            }
        }
    }
}

@Composable
fun CallLogDetailsPage(callLogDetailsInfo: CallLogDetailsInfo) {
    val date = DateFormat.format("dd/MM/yyyy", callLogDetailsInfo.date)

    Column {
        Text(text = date.toString())
        Text(text = callLogDetailsInfo.duration.toString())
        Text(text = callLogDetailsInfo.type.toString())
    }
}