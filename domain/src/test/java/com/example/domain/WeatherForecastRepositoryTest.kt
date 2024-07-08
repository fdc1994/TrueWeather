package com.example.domain

import com.example.domain.data.LocalizationManager
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.domain.data.objects.*
import com.example.domain.data.repositories.WeatherForecastRepositoryImpl
import com.example.network.data.PrecipitationIntensity
import com.example.network.data.UserPreferences
import com.example.network.data.WeatherType
import com.example.network.data.WindSpeed
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.UserPreferencesDataStore
import com.example.network.utils.TimestampUtil
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class WeatherForecastRepositoryImplTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var ipmaService: IPMAService

    @Mock
    private lateinit var weatherForecastMappers: WeatherForecastMappers

    @Mock
    private lateinit var localizationManager: LocalizationManager

    @Mock
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    private lateinit var weatherForecastRepository: WeatherForecastRepositoryImpl

    @Before
    fun setup() {
        weatherForecastRepository = WeatherForecastRepositoryImpl(
            ipmaService,
            weatherForecastMappers,
            localizationManager,
            userPreferencesDataStore
        )
    }

    @Test
    fun `getWeatherForecast should return result with current location data`() = runTest {
        mockkObject(TimestampUtil)
        every { TimestampUtil.isBeforeToday("2022-04-04") } returns false
        whenever(localizationManager.checkPermissions()).thenReturn(true)
        whenever(localizationManager.getLastKnownLocation()).thenReturn(mockLocationData())
        whenever(ipmaService.getWeatherData(any())).thenReturn(mock())
        whenever(weatherForecastMappers.mapWeatherResponse(any())).thenReturn(mockWeatherForecast())
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(mutableListOf()))

        val result = weatherForecastRepository.getWeatherForecast()

        assertTrue(result.resultList.isNotEmpty())
        assertEquals(WeatherFetchStatus.SUCCESS, result.resultList[0].status)
    }

    @Test
    fun `getWeatherForecast should return permission error when permissions are not granted`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(false)
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(mutableListOf()))

        val result = weatherForecastRepository.getWeatherForecast()

        assertTrue(result.resultList.isNotEmpty())
        assertEquals(WeatherFetchStatus.PERMISSION_ERROR, result.resultList[0].status)
    }

    @Test
    fun `getWeatherForecast should return not in country error when location data is invalid`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(true)
        whenever(localizationManager.getLastKnownLocation()).thenReturn(null)
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(mutableListOf()))

        val result = weatherForecastRepository.getWeatherForecast()

        assertTrue(result.resultList.isNotEmpty())
        assertEquals(WeatherFetchStatus.NOT_IN_COUNTRY_ERROR, result.resultList[0].status)
    }

    @Test
    fun `trySearchWeatherForecast should return search result for valid query`() = runTest {
        val searchQuery = "Lisbon"
        mockkObject(TimestampUtil)
        every { TimestampUtil.isBeforeToday("2022-04-04") } returns false
        whenever(localizationManager.mapAddressNameToMultipleDistrictIdentifier(searchQuery)).thenReturn(mutableListOf(mockLocationData()))
        whenever(ipmaService.getWeatherData(any())).thenReturn(mock())
        whenever(weatherForecastMappers.mapWeatherResponse(any())).thenReturn(mockWeatherForecast())
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(mutableListOf()))

        val result = weatherForecastRepository.trySearchWeatherForecast(searchQuery)

        assertTrue(result.resultList.isNotEmpty())
        assertEquals(WeatherFetchStatus.SUCCESS, result.resultList[0].status)
    }

    @Test
    fun `trySearchWeatherForecast should return empty result for invalid query`() = runTest {
        val searchQuery = "UnknownPlace"
        whenever(localizationManager.mapAddressNameToMultipleDistrictIdentifier(searchQuery)).thenReturn(emptyList())

        val result = weatherForecastRepository.trySearchWeatherForecast(searchQuery)

        assertTrue(result.resultList.isEmpty())
    }

    private fun mockLocationData(): LocationData = LocationData(
        1,
        "1",
        1,
        1,
        "1",
        1,
        "local",
        "longitude"
    )

    private fun mockWeatherData() = WeatherData(
        precipitaProb = "10",
        tMin = "15",
        tMax = "25",
        predWindDir = "N",
        weatherType = WeatherType.FOG,
        classWindSpeed = WindSpeed.WEAK,
        classPrecInt = PrecipitationIntensity.WEAK,
        longitude = "longitude",
        forecastDate = "2022-04-04",
        latitude = "latitude",
    )

    private fun mockWeatherForecast() = WeatherForecast(
        "owner",
        "Portugal",
        mutableListOf(mockWeatherData()),
        1,
        "2024-04-04"
    )
}