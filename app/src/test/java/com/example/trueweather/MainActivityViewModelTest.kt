package com.example.trueweather

import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.ResultWrapper
import com.example.trueweather.main.MainActivityViewModel
import com.example.trueweather.persistence.WeatherResultDataStore
import com.example.trueweather.utils.NetworkConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainActivityViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var weatherForecastRepository: WeatherForecastRepository

    @Mock
    private lateinit var weatherResultDataStore: WeatherResultDataStore

    @Mock
    private lateinit var networkConnectivityManager: NetworkConnectivityManager

    private lateinit var viewModel: MainActivityViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainActivityViewModel(weatherForecastRepository, weatherResultDataStore, networkConnectivityManager)
    }

    @Test
    fun `when permission is granted, loadData is called`() = runTest {
        whenever(networkConnectivityManager.hasInternetConnection()).thenReturn(true)
        viewModel.updatePermissionState(true)
        verify(weatherForecastRepository, atLeastOnce()).getWeatherForecast()
    }

    @Test
    fun `when permission is not granted, loadData is not called`() = runTest {
        viewModel.updatePermissionState(false)
        assertFalse(viewModel.weatherState.first() is ResultWrapper.Success)
        verify(weatherForecastRepository, never()).getWeatherForecast()
    }

    @Test
    fun `loadData with internet connection sets weatherState to Success`() = runTest {
        val weatherResult = WeatherResult(resultList = listOf(mock()))
        whenever(networkConnectivityManager.hasInternetConnection()).thenReturn(true)
        whenever(weatherForecastRepository.getWeatherForecast()).thenReturn(weatherResult)

        viewModel.loadData()

        assertEquals(ResultWrapper.Success(weatherResult), viewModel.weatherState.first())
    }

    @Test
    fun `loadData without internet connection sets weatherState to Success from datastore`() = runTest {
        val weatherResult = WeatherResult(resultList = listOf(mock()))
        whenever(networkConnectivityManager.hasInternetConnection()).thenReturn(false)
        whenever(weatherResultDataStore.getWeatherForecast()).thenReturn(weatherResult)

        viewModel.loadData()

        assertEquals(ResultWrapper.Success(weatherResult), viewModel.weatherState.first())
    }

    @Test
    fun `loadData sets weatherState to Error on exception`() = runTest {
        `when`(networkConnectivityManager.hasInternetConnection()).thenReturn(true)
        `when`(weatherForecastRepository.getWeatherForecast()).thenThrow(RuntimeException())

        viewModel.loadData()

        assertTrue(viewModel.weatherState.first() is ResultWrapper.Error)
        assertEquals(ErrorType.NETWORK_ERROR, (viewModel.weatherState.first() as ResultWrapper.Error).errorType)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}