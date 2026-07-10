package com.example.acapp.ui

import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
import com.example.acapp.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

private val raymond = Villager(
    id = "raymond",
    name = "Raymond",
    species = "cat",
    personality = "smug",
    gender = "male",
    birthdayMonth = 10,
    birthdayDay = 1,
)

private class FakeRepo(
    private val listResult: Result<List<Villager>>,
    private val detailResult: Result<Villager> = Result.success(raymond),
) : VillagerRepository {
    override suspend fun listVillagers(): List<Villager> = listResult.getOrThrow()
    override suspend fun getVillager(id: String): Villager = detailResult.getOrThrow()
}

@OptIn(ExperimentalCoroutinesApi::class)
class VillagersListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() = Dispatchers.setMain(dispatcher)

    @AfterTest
    fun teardown() = Dispatchers.resetMain()

    @Test
    fun `successful load transitions from Loading to Success`() = runTest(dispatcher) {
        val vm = VillagersListViewModel(FakeRepo(Result.success(listOf(raymond))))
        assertIs<UiState.Loading>(vm.state.value)

        dispatcher.scheduler.advanceUntilIdle()

        val terminal = vm.state.value
        assertIs<UiState.Success<List<Villager>>>(terminal)
        assertEquals(listOf(raymond), terminal.value)
    }

    @Test
    fun `failed load produces Error state with message`() = runTest(dispatcher) {
        val vm = VillagersListViewModel(FakeRepo(Result.failure(RuntimeException("boom"))))
        dispatcher.scheduler.advanceUntilIdle()

        val terminal = vm.state.value
        assertIs<UiState.Error>(terminal)
        assertTrue("boom" in terminal.message)
    }
}
