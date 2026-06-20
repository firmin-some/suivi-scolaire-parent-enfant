package bf.ujkz.suiviscolaireparent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bf.ujkz.suiviscolaireparent.repository.AuthRepository
import bf.ujkz.suiviscolaireparent.ui.dashboard.DashboardScreen
import bf.ujkz.suiviscolaireparent.ui.login.LoginScreen
import bf.ujkz.suiviscolaireparent.ui.theme.SuiviScolaireParentTheme
import bf.ujkz.suiviscolaireparent.ui.verify.VerifyChildScreen
import kotlinx.coroutines.launch
import bf.ujkz.suiviscolaireparent.ui.notes.NotesScreen
import bf.ujkz.suiviscolaireparent.ui.paiements.PaiementsScreen
import bf.ujkz.suiviscolaireparent.ui.absences.AbsencesScreen
import bf.ujkz.suiviscolaireparent.ui.annonces.AnnoncesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuiviScolaireParentTheme {
                MainApp()
            }
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

private val bottomNavItems = listOf(
    BottomNavItem("dashboard", "Accueil", Icons.Default.Home),
    BottomNavItem("notes", "Notes", Icons.Default.List),
    BottomNavItem("paiements", "Paiements", Icons.Default.ShoppingCart),
    BottomNavItem("absences", "Absences", Icons.Default.DateRange),
    BottomNavItem("annonces", "Annonces", Icons.Default.Notifications)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository(context) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showAppBars = currentRoute != null && currentRoute != "login" && currentRoute != "verify"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showAppBars) {
                TopAppBar(
                    title = { Text(bottomNavItems.find { it.route == currentRoute }?.label ?: "Suivi scolaire") },
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                authRepository.logout()
                                navController.navigate("login") {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                            }
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showAppBars) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo("dashboard") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("verify") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("verify") {
                VerifyChildScreen(onVerified = {
                    navController.navigate("dashboard") {
                        popUpTo("verify") { inclusive = true }
                    }
                })
            }
            composable("dashboard") {
                DashboardScreen(onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable("notes") { NotesScreen() }
            composable("paiements") { PaiementsScreen() }
            composable("absences") { AbsencesScreen() }
            composable("annonces") { AnnoncesScreen() }
        }
    }
}

@Composable
fun PlaceholderScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}