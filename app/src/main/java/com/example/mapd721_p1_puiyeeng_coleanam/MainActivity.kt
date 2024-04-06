package com.example.mapd721_p1_puiyeeng_coleanam

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapd721_p1_puiyeeng_coleanam.ui.theme.MAPD721P1PuiYeeNgColeAnamTheme
import kotlinx.coroutines.launch
import com.example.mapd721_p1_puiyeeng_coleanam.datastore.StoreProductInfo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAPD721P1PuiYeeNgColeAnamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

data class Product(val productId: Int, val productName: String, val price: Double, var quantity: Int, val imagePath: String)

@Composable
fun MainScreen() {

    // context
    val context = LocalContext.current
    // scope
    val scope = rememberCoroutineScope()
    // datastore Product
    val dataStore = StoreProductInfo(context)

    val GroceriesList : List<Product> = listOf(
        Product(
            productId = 1,
            productName = "Watermelon",
            price = 2.99,
            imagePath = "watermelon",
            quantity = 0
        ),
        Product(
            productId = 2,
            productName = "Orange",
            price = 0.99,
            imagePath = "orange",
            quantity = 0
        ),
        Product(
            productId = 3,
            productName = "Banana",
            price = 1.69,
            imagePath = "banana",
            quantity = 0
        ),
        Product(
            productId = 4,
            productName = "Strawberry",
            price = 3.69,
            imagePath = "strawberry",
            quantity = 0
        ),
        Product(
            productId = 5,
            productName = "Blueberry",
            price = 2.99,
            imagePath = "blueberry",
            quantity = 0
        ),
        Product(
            productId = 6,
            productName = "Apple",
            price = 2.99,
            imagePath = "apple",
            quantity = 0
        )
    )

    val addedProducts = remember { mutableStateListOf<Product>() }

    var retrievedProducts by remember { mutableStateOf(
        emptyList<Product>()
    ) }

    // State to track if dialog is shown
    var showDialog by remember { mutableStateOf(false) }

    // LaunchedEffect to perform data loading
    LaunchedEffect(Unit) {
        retrievedProducts = dataStore.readProducts()
        addedProducts.clear()
        addedProducts.addAll(retrievedProducts)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Easy Grocery",
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    scope.launch {
                        retrievedProducts = dataStore.readProducts()
                        showDialog = true

                    }
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart"
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        dataStore.clearProducts()
                        addedProducts.clear()
                    }
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(text = "Clear")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        if (showDialog) {
            ProductListDialog(
                products = retrievedProducts,
                onDismiss = { showDialog = false }
            )
        }
        ProductList(products = GroceriesList,
            addToCart = {
                product ->
//            addedProducts.add(product)
            scope.launch {
                dataStore.saveProducts(product)
            }
        })
    }
}


@Composable
fun ProductList(products: List<Product>,  addToCart: (Product) -> Unit) {
    LazyColumn {
        items(products) { product ->
            ProductItem(product, addToCart = { addToCart(product)})
        }
    }
}

@Composable
fun ProductItem(product: Product, addToCart: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
//            Image(
//                painter = rememberCoilPainter(product.imageUrl),
//                contentDescription = product.productName,
//                modifier = Modifier.size(64.dp)
//            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = product.productName,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(text = product.price.toString())
                Button(onClick = addToCart, Modifier
                    .align(Alignment.End)) {
                    Text("Add to Cart")
                }


            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Product ID : ${product.productId}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Product Name : ${product.productName}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: $${product.price}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${product.quantity}")
        }
    }
}


@Composable
fun ProductListDialog(
    products: List<Product>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Shopping Cart") },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Close")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                LazyColumn {
                    items(products) { product ->
                        ProductCard(product = product)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MAPD721P1PuiYeeNgColeAnamTheme {
        MainScreen()
    }
}