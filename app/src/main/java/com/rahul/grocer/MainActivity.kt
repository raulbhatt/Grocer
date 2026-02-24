package com.rahul.grocer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahul.grocer.ui.theme.OrbitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrbitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OrbitApp()
                }
            }
        }
    }
}

@Composable
fun OrbitApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (currentRoute != "onboarding" && currentRoute != "splash" && currentRoute != "login" && currentRoute != "registration") {
                OrbitBottomBar(navController, currentRoute)
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                com.rahul.grocer.feature.splash.SplashScreen(
                    onSplashComplete = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            composable("login") {
                com.rahul.grocer.feature.login.LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToRegistration = {
                        navController.navigate("registration")
                    }
                )
            }
            composable("registration") {
                com.rahul.grocer.feature.registration.RegistrationScreen(
                    onRegistrationSuccess = {
                        navController.navigate("home") {
                            popUpTo("registration") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable("onboarding") {
                com.rahul.grocer.feature.onboarding.OnboardingScreen(
                    onOnboardingComplete = {
                        navController.navigate("home") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                com.rahul.grocer.feature.home.HomeScreen(
                    onNavigateToTracker = { navController.navigate("tracker") },
                    onNavigateToZeroWaste = { navController.navigate("zero_waste") },
                    onNavigateToSupport = { navController.navigate("support") }
                )
            }
            composable("search") {
                com.rahul.grocer.feature.search.SearchScreen()
            }
            composable("cart") {
                com.rahul.grocer.feature.cart.CartScreen()
            }
            composable("tracker") {
                com.rahul.grocer.feature.tracker.TrackerScreen()
            }
            composable("zero_waste") {
                com.rahul.grocer.feature.ar.ZeroWasteScreen()
            }
            composable("profile") {
                com.rahul.grocer.feature.profile.ProfileScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("support") {
                com.rahul.grocer.feature.support.SupportScreen(
                    onNavigateToOrbitAI = { navController.navigate("orbit_ai") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("orbit_ai") {
                com.rahul.grocer.feature.support.OrbitAIScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun OrbitBottomBar(navController: androidx.navigation.NavController, currentRoute: String?) {
    androidx.compose.material3.NavigationBar(
        containerColor = com.rahul.grocer.ui.theme.DeepSpaceBlue.copy(alpha = 0.9f),
        contentColor = com.rahul.grocer.ui.theme.StarlightSilver
    ) {
        val items = listOf("home" to "Home", "search" to "Search", "cart" to "Cart", "profile" to "Profile")
        val icons = listOf(
            androidx.compose.material.icons.Icons.Default.Home,
            androidx.compose.material.icons.Icons.Default.Search,
            androidx.compose.material.icons.Icons.Default.ShoppingCart,
            androidx.compose.material.icons.Icons.Default.Person
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(icons[index], contentDescription = item.second) },
                label = { androidx.compose.material3.Text(item.second) },
                selected = currentRoute == item.first,
                onClick = {
                    navController.navigate(item.first) {
                        popUpTo("home") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = com.rahul.grocer.ui.theme.NebulaPurple,
                    selectedTextColor = com.rahul.grocer.ui.theme.NebulaPurple,
                    indicatorColor = com.rahul.grocer.ui.theme.GlassWhite
                )
            )
        }
    }
}

