package com.example.fetch_code_challenge

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.fetch_code_challenge.Repo.FetchApi
import com.example.fetch_code_challenge.Repo.RetrofitHelper
import com.example.fetch_code_challenge.sampledata.Model.FetchData
import com.example.fetch_code_challenge.sampledata.Model.FetchDataItem
import com.example.fetch_code_challenge.ui.theme.Fetch_code_challengeTheme
import com.example.fetch_code_challenge.viewmodel.FetchViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var groupedData: Map<Int, List<FetchDataItem>>? = null

        GlobalScope.launch {
            val fetchApi: FetchApi = RetrofitHelper.getInstance().create(FetchApi::class.java)
            val result: Response<FetchData> = fetchApi.getFetchData()
            if (result.isSuccessful) {
                val filteredData = result.body()?.filterNot { it.name == null || it.name == "" }
                //Log.d(TAG, "Filtered Data: $filteredData")
                val sortedData = filteredData?.sortedWith(compareBy<FetchDataItem> { it.listId }.thenBy { it.name })
                //Log.d(TAG, "sorted data $sortedData")
                groupedData = sortedData?.let { fetchDataList -> fetchDataList.groupBy { it.listId!! } }
                //Log.d(TAG, "grouped data: $groupedData")
            }
        }

        enableEdgeToEdge()
        setContent {
            val pagerState = rememberPagerState(pageCount = groupedData?.keys?.size ?: 0)

//            val fetchViewModel = FetchViewModel()
//            fetchViewModel.getFetchData()
            Fetch_code_challengeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { FetchTopBar() }
                ) {
                    Column(modifier = Modifier.background(Color.White).padding(it)) {
                        Tabs(groupedData, pagerState)
                        TabsContent(pagerState = pagerState, groupedData = groupedData)
                    }
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding),
////                        fetchViewModel = fetchViewModel
//                    )



                }
            }
        }


    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    fetchViewModel: FetchViewModel = FetchViewModel()
) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@ExperimentalPagerApi
@Composable
fun FetchTopBar() {
//    val pagerState = rememberPagerState(pageCount = pageCount) )
    Column(modifier = Modifier.background(Color.White)) {
        TopAppBar(backgroundColor = Color.White) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Fetch Code Challenge",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        18F,
                        TextUnitType.Sp
                    ),
                    // on below line we are specifying a modifier
                    // to our text and adding passing from all sides.
                    modifier = Modifier.padding(all = Dp(5F)),
                    textAlign = TextAlign.Center
                )
            }
        }
//        Tabs(pagerState = pagerState)
    }
}

// on below line we are
// creating a function for tabs
@ExperimentalPagerApi
@Composable
fun Tabs(groupedData: Map<Int, List<FetchDataItem>>?, pagerState: PagerState) {

    // on below line we are creating
    // a list of tabs
    val list = listOf(
        "Home" to Icons.Default.Face,
        "Shopping" to Icons.Default.ShoppingCart,
        "Settings" to Icons.Default.Settings,
        "Profile" to Icons.Default.Person
    )

    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        // on below line we are specifying
        // the selected index
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.PrimaryIndicator(
                Modifier.pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPositions
                ),
                height = 2.dp,
                color = Color.White
            )
        },
        tabs = {
            groupedData?.keys?.forEachIndexed { index, _ ->
                Tab(
                    icon = { Icon(imageVector = list[index].second, contentDescription = null) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = @Composable {
                        Text(
                            text = "List $index",
                            // on below line we are specifying the text color
                            // for the text in that tab
                            color = if (pagerState.currentPage == index) Color.Black else Color.LightGray
                        )
                    }

                )
            }
//            list.forEachIndexed { index, _ ->
//                Tab(
//                    icon = { Icon(imageVector = list[index].second, contentDescription = null) },
//                    selected = pagerState.currentPage == index,
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(index)
//                        }
//                    },
//                    text = @Composable {
//                        Text(
//                            text = list[index].first,
//                            // on below line we are specifying the text color
//                            // for the text in that tab
//                            color = if (pagerState.currentPage == index) Color.White else Color.LightGray
//                        )
//                    }
//
//                )
//            }
        }
    )
}

// on below line we are creating a tab content method
// in which we will be displaying the individual page of our tab .
@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    groupedData: Map<Int, List<FetchDataItem>>?
) {
    // on below line we are creating
    // horizontal pager for our tab layout.
    HorizontalPager(state = pagerState) {
        // on below line we are specifying
        // the different pages.
            page ->

        groupedData?.keys?.forEach { it ->
            LazyFetchList(fetchDataList = groupedData[page])
        }

//        when (page) {
//            // on below line we are calling tab content screen
//            // and specifying data as Home Screen.
//            0 -> TabContentScreen(data = "Welcome to Home Screen")
//            // on below line we are calling tab content screen
//            // and specifying data as Shopping Screen.
//            1 -> TabContentScreen(data = "Welcome to Shopping Screen")
//            // on below line we are calling tab content screen
//            // and specifying data as Settings Screen.
//            2 -> TabContentScreen(data = "Welcome to Settings Screen")
//        }


    }
}


// on below line we are creating a Tab Content
// Screen for displaying a simple text message.
@Composable
fun TabContentScreen(data: String) {
    // on below line we are creating a column
    Column(
        // in this column we are specifying modifier
        // and aligning it center of the screen on below lines.
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // in this column we are specifying the text
        Text(
            // on below line we are specifying the text message
            text = data,

            // on below line we are specifying the text style.
            style = MaterialTheme.typography.bodyMedium,

            // on below line we are specifying the text color
//            color = greenColor,

            // on below line we are specifying the font weight
            fontWeight = FontWeight.Bold,

            //on below line we are specifying the text alignment.
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun FetchCardItem(fetchDataItem: FetchDataItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
//                .padding(horizontal = 8.dp)
            .fillMaxWidth(1f),
        shape = CardDefaults.outlinedShape

//            horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = modifier.padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.Person, contentDescription = "Person Icon")
            Text(
                modifier = modifier.padding(bottom = 4.dp),
                text = "Id: " + fetchDataItem.id.toString()
            )
            Text(
                modifier = modifier.padding(top = 4.dp),
                text = "Name: " + fetchDataItem.name.toString()
            )
        }

    }
//    Row(modifier = Modifier.padding(all = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
//        Icon(Icons.Default.Person, contentDescription = "Person Icon")
//        Card(
//            modifier = Modifier
////                .padding(horizontal = 8.dp)
//                .fillMaxWidth(1f),
//            shape = CardDefaults.outlinedShape
//
////            horizontalAlignment = Alignment.Start
//        ) {
//            Text(
//                modifier = Modifier.padding(bottom = 4.dp),
//                text = "Id: " + fetchDataItem.id.toString()
//            )
//            Text(
//                modifier = Modifier.padding(top = 4.dp),
//                text = "Name: " + fetchDataItem.name.toString()
//            )
//        }
//    }
}

@Composable
fun LazyFetchList(fetchDataList: List<FetchDataItem>?) {
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp)) {
        fetchDataList?.size?.let {
            items(it) {
                fetchDataList.forEach {
                    fetchDataItem ->
                    FetchCardItem(fetchDataItem = fetchDataItem)
                }
                //            FetchCardItem(fetchDataItem = FetchDataItem(1, 1, "Test1"))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val groupedData = mapOf(
        1 to listOf(FetchDataItem(1, 1, "Test1")),
        2 to listOf(FetchDataItem(2, 2, "Test2")),
        3 to listOf(FetchDataItem(3, 3, "Test3")),
        4 to listOf(FetchDataItem(4, 4, "Test4")),
        5 to listOf(FetchDataItem(1, 1, "Test1")),
        6 to listOf(FetchDataItem(2, 2, "Test2")),
        7 to listOf(FetchDataItem(3, 3, "Test3")),
        8 to listOf(FetchDataItem(4, 4, "Test4"))
    )

    Tabs(pagerState = rememberPagerState(8), groupedData = groupedData)
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabContentPreview() {
    val groupedData = mapOf(
        1 to listOf(FetchDataItem(1, 1, "Test1")),
        2 to listOf(FetchDataItem(2, 2, "Test2")),
        3 to listOf(FetchDataItem(3, 3, "Test3")),
        4 to listOf(FetchDataItem(4, 4, "Test4"))
    )
    TabsContent(pagerState = rememberPagerState(4), groupedData = groupedData)
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun FetchTabLayoutPreview() {
    FetchTopBar()
}

@Preview(showBackground = true)
@Composable
fun FetchCardItemPreview() {
    FetchCardItem(fetchDataItem = FetchDataItem(1, 1, "Test1"))
    FetchCardItem(fetchDataItem = FetchDataItem(2, 2, "Test2"))
}

@Preview(showBackground = true)
@Composable
fun LazyFetchListPreview() {
    val fetchDataList = listOf(
        FetchDataItem(1, 1, "Test1"),
        FetchDataItem(2, 2, "Test2"),
        FetchDataItem(3, 3, "Test3"),
        FetchDataItem(4, 4, "Test4")
    )

    LazyFetchList(fetchDataList = fetchDataList)

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Fetch_code_challengeTheme {
//        Greeting("Android")
//    }
//}