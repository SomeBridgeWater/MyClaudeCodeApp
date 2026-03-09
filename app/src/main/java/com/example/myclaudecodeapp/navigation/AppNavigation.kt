package com.example.myclaudecodeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myclaudecodeapp.Routes
import com.example.myclaudecodeapp.ui.login.LoginScreen
import com.example.myclaudecodeapp.ui.main.MainScreen

/** ナビゲーションルート定義 */
//object AppRoutes {
//    const val LOGIN = "login"
//    const val MAIN = "main"
//}

/**
 * アプリ全体のナビゲーションホスト
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LoginRoute
    ) {
        composable<Routes.LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    /*
                    navController.navigate(AppRoutes.MAIN) {
                        // ログイン画面をバックスタックから除去（戻れなくする）
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                     */
                    navController.navigate(Routes.MainRoute(id = "")) {
                        // ログイン画面をバックスタックから除去（戻れなくする）
                        popUpTo(Routes.LoginRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<Routes.MainRoute> { backStackEntry ->
            val main: Routes.MainRoute = backStackEntry.toRoute()
            MainScreen(main.id)
        }
    }
}
