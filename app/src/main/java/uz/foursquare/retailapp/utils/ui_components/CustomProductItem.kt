package uz.foursquare.retailapp.utils.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.foursquare.retailapp.R
import uz.foursquare.retailapp.di.App
import uz.foursquare.retailapp.ui.goods.goods_screen.types.GoodType
import uz.foursquare.retailapp.ui.theme.AppTheme
import uz.foursquare.retailapp.utils.convertToPriceFormat
import java.util.Locale

@Composable
fun CustomProductItem(goodItem: GoodType, index: Int, onClick: (Int) -> Unit) {
    var image = "https://picsum.photos/200"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(index)
            }
    ) {
        AsyncImage(
            model = image, // Use actual image URL
            contentDescription = "Good image",
            placeholder = painterResource(id = R.drawable.image_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(1f) // Ensures text takes available space
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.brand_icon),
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 4.dp)
                )

                Text(
                    text = goodItem.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.headlineH3,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(R.drawable.currency_usd_icon),
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 4.dp)
                )

                Text(
                    text = goodItem.salePrice.convertToPriceFormat(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.headlineH4,
                    color = AppTheme.color.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(R.drawable.barcode_scanner),
                    contentDescription = null,
                    tint = AppTheme.color.primary,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 4.dp)
                )

                Text(
                    text = goodItem.barcode,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.bodyM
                )
            }


        }

        Column(modifier = Modifier.fillMaxHeight()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.appColor.neutralLightMedium),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row {
                        Text(
                            text = goodItem.count.toString(),
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        )
                        Text(
                            text = " ${goodItem.uniteType.lowercase()}",
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                            maxLines = 1,
                        )
                    }

                }
            }
        }


    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) // Ensure proper placement
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f))
    ) {
        CircularProgressIndicator(color = AppTheme.color.primary)
    }
}

@Composable
fun EmptyListMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = message, textAlign = TextAlign.Center)
    }
}
