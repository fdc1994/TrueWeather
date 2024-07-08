package com.example.trueweather


import com.example.domain.data.LocalizationManager
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.network.data.WeatherLocationDTO
import com.example.trueweather.splash.SplashActivityViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SplashActivityViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var districtIdentifiersRepository: DistrictIdentifiersRepository

    @Mock
    private lateinit var localizationManager: LocalizationManager

    private lateinit var viewModel: SplashActivityViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SplashActivityViewModel(districtIdentifiersRepository, localizationManager)
    }

    @Test
    fun `loadData navigates to AskForPermissions when permissions are not granted`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(false)

        viewModel.loadData()

        assertEquals(
            SplashActivityViewModel.NavigationState.AskForPermissions,
            viewModel.navigationState.first()
        )
    }

    @Test
    fun `loadData navigates to NavigateToMain on successful data fetch`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(true)
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenReturn(WeatherLocationDTO("", "", mutableListOf()))

        viewModel.loadData()

        assertEquals(
            SplashActivityViewModel.NavigationState.NavigateToMain,
            viewModel.navigationState.first()
        )
    }

    @Test
    fun `loadData does not navigate on data fetch exception`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(true)
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenThrow(RuntimeException())

        viewModel.loadData()

        assertEquals(null, viewModel.navigationState.first())
    }

    @Test
    fun `loadData does not navigate on null data fetch`() = runTest {
        whenever(localizationManager.checkPermissions()).thenReturn(true)
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenReturn(null)

        viewModel.loadData()

        assertEquals(null, viewModel.navigationState.first())
    }

    @Test
    fun `resumeLoadAfterPermissions navigates to NavigateToMain on successful data fetch`() = runTest {
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenReturn(WeatherLocationDTO("", "", mutableListOf()))

        viewModel.resumeLoadAfterPermissions()

        assertEquals(
            SplashActivityViewModel.NavigationState.NavigateToMain,
            viewModel.navigationState.first()
        )
    }

    @Test
    fun `resumeLoadAfterPermissions does not navigate on data fetch exception`() = runTest {
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenThrow(RuntimeException())

        viewModel.resumeLoadAfterPermissions()

        assertEquals(null, viewModel.navigationState.first())
    }

    @Test
    fun `resumeLoadAfterPermissions does not navigate on null data fetch`() = runTest {
        whenever(districtIdentifiersRepository.getDistrictIdentifiersList()).thenReturn(null)

        viewModel.resumeLoadAfterPermissions()

        assertEquals(null, viewModel.navigationState.first())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}