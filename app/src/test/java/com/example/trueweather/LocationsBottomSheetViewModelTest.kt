package com.example.trueweather

import com.example.domain.data.objects.LocationData

import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultWrapper
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.network.data.UserPreferences
import com.example.network.persistence.UserPreferencesDataStore
import com.example.trueweather.main.addlocation.LocationsBottomSheetViewModel
import com.example.trueweather.persistence.WeatherResultDataStore
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class LocationsBottomSheetViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var weatherForecastRepository: WeatherForecastRepository

    @Mock
    private lateinit var weatherForecastDataStore: WeatherResultDataStore

    @Mock
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    private lateinit var viewModel: LocationsBottomSheetViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LocationsBottomSheetViewModel(weatherForecastRepository, weatherForecastDataStore, userPreferencesDataStore)
    }

    @Test
    fun `loadData sets state to UserLocationsSuccess`() = runTest {
        val weatherResult = WeatherResult(resultList = listOf())
        whenever(weatherForecastDataStore.getWeatherForecast()).thenReturn(weatherResult)

        viewModel.loadData()

        assertEquals(LocationsBottomSheetViewModel.LocationsState.UserLocationsSuccess(weatherResult), viewModel.locationsState.first())
    }

    @Test
    fun `loadData sets state to Error on exception`() = runTest {
        whenever(weatherForecastDataStore.getWeatherForecast()).thenThrow(RuntimeException())

        viewModel.loadData()

        assertEquals(LocationsBottomSheetViewModel.LocationsState.Error, viewModel.locationsState.first())
    }

    @Test
    fun `searchLocations sets state to SearchLocationsSuccess`() = runTest {
        val weatherResult = WeatherResult(resultList = listOf())
        whenever(weatherForecastRepository.trySearchWeatherForecast("query")).thenReturn(weatherResult)

        viewModel.searchLocations("query")

        assertEquals(LocationsBottomSheetViewModel.LocationsState.SearchLocationsSuccess(weatherResult), viewModel.locationsState.first())
    }

    @Test
    fun `searchLocations sets state to Error on exception`() = runTest {
        whenever(weatherForecastRepository.trySearchWeatherForecast("query")).thenThrow(RuntimeException())

        viewModel.searchLocations("query")

        assertEquals(LocationsBottomSheetViewModel.LocationsState.Error, viewModel.locationsState.first())
    }

    @Test
    fun `addLocation sets state to IOSuccess`() = runTest {
        val weatherResultWrapper = WeatherResultWrapper(address = mockAddress("local", 1), status = WeatherFetchStatus.SUCCESS)
        `when`(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(listOf()))
        `when`(userPreferencesDataStore.saveUserPreferences(any())).thenReturn(true)
        `when`(weatherForecastDataStore.getWeatherForecast()).thenReturn(WeatherResult(resultList = listOf()))
        `when`(weatherForecastDataStore.saveWeatherForecast(any())).thenReturn(true)

        viewModel.addLocation(weatherResultWrapper)

        assertEquals(
            LocationsBottomSheetViewModel.LocationsState.IOSuccess(isAdd = true, locationName = "local"),
            viewModel.locationsState.first()
        )
    }


    @Test
    fun `addLocation sets state to IOError on failure`() = runTest {
        val weatherResultWrapper = WeatherResultWrapper(address = mockAddress("local", 1), status = WeatherFetchStatus.SUCCESS)
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(listOf()))
        whenever(userPreferencesDataStore.saveUserPreferences(any())).thenReturn(false)

        viewModel.addLocation(weatherResultWrapper)

        assertEquals(
            LocationsBottomSheetViewModel.LocationsState.IOError(isAdd = true, locationName = "local"),
            viewModel.locationsState.first()
        )
    }

    @Test
    fun `removeLocation sets state to IOSuccess`() = runTest {
        val weatherResultWrapper = WeatherResultWrapper(address = mockAddress("local", 1), status = WeatherFetchStatus.SUCCESS)
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(listOf("1")))
        whenever(userPreferencesDataStore.saveUserPreferences(any())).thenReturn(true)
        whenever(weatherForecastDataStore.getWeatherForecast()).thenReturn(WeatherResult(resultList = listOf(weatherResultWrapper)))
        whenever(weatherForecastDataStore.saveWeatherForecast(any())).thenReturn(true)

        viewModel.removeLocation(weatherResultWrapper)

        assertEquals(
            LocationsBottomSheetViewModel.LocationsState.UserLocationsSuccess::class.java,
            viewModel.locationsState.first()::class.java
        )
    }

    @Test
    fun `removeLocation sets state to IOError on failure`() = runTest {
        val weatherResultWrapper = WeatherResultWrapper(address = mockAddress("local", 1), status = WeatherFetchStatus.SUCCESS)
        whenever(userPreferencesDataStore.getUserPreferences()).thenReturn(UserPreferences(mutableListOf("1")))
        whenever(userPreferencesDataStore.saveUserPreferences(any())).thenReturn(false)

        viewModel.removeLocation(weatherResultWrapper)

        assertEquals(
            LocationsBottomSheetViewModel.LocationsState.IOError(isAdd = false, locationName = "local"),
            viewModel.locationsState.first()
        )
    }

    private fun mockAddress(mockLocal: String, globalId: Int): LocationData {
        val address = mock(LocationData::class.java)
        whenever(address.local).thenReturn(mockLocal)
        whenever(address.globalIdLocal).thenReturn(globalId)
        return address
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
