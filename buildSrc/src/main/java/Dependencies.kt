/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
object Dependencies {

    const val hilt                  = "com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}"

    const val core                  = "androidx.core:core-ktx:${Version.core}"
    const val appcompat             = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val material              = "com.google.android.material:material:${Version.material}"
    const val constraintLayout      = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val livedataKtx           = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.livedataKtx}"
    const val viewModelKtx          = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.viewModelKtx}"
    const val navigationFragKtx     = "androidx.navigation:navigation-fragment-ktx:${Version.navigationFragKtx}"
    const val navigationUiKtx       = "androidx.navigation:navigation-ui-ktx:${Version.navigationUiKtx}"
    const val paging                = "androidx.paging:paging-runtime-ktx:${Version.paging}"

    const val hiltAndroid           = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltCompiler          = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val hiltAndroidTest       = "com.google.dagger:hilt-android-testing:${Version.hilt}"

    const val dataStore             = "androidx.datastore:datastore-preferences:${Version.dataStore}"

    const val jUnit                 = "junit:junit:${Version.jUnit}"
    const val extJUnit              = "androidx.test.ext:junit:${Version.extJUnit}"
    const val espresso              = "androidx.test.espresso:espresso-core:${Version.espresso}"

    const val mockitoCore           = "org.mockito:mockito-core:${Version.mockito}"
    const val mockitoInline         = "org.mockito:mockito-inline:${Version.mockito}"

    const val coreTest              = "androidx.arch.core:core-testing:${Version.coreTest}" // InstantTaskExecutorRule
    const val coroutinesTest        = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}" //TestCoroutineDispatcher

    const val truth                 = "com.google.truth:truth:${Version.truth}"
    const val turbine               = "app.cash.turbine:turbine:${Version.turbine}"

    const val retrofit              = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val converterGson         = "com.squareup.retrofit2:converter-gson:${Version.converterGson}"

    const val okhttp                = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
    const val loggingInterceptor    = "com.squareup.okhttp3:logging-interceptor:${Version.loggingInterceptor}"

    const val coroutinesCore        = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val coroutines            = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"

    const val glide                 = "com.github.bumptech.glide:glide:${Version.glide}"
    const val glideCompiler         = "com.github.bumptech.glide:compiler:${Version.glide}"

    const val servicesMaps          = "com.google.android.gms:play-services-maps:${Version.servicesMaps}"
    const val servicesLocation      = "com.google.android.gms:play-services-location:${Version.servicesLocation}"
}