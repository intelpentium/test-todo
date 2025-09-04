//package com.projeku.myapplication.domain.usecase
//
//import com.projeku.myapplication.domain.model.ToDo
//import com.projeku.myapplication.domain.repository.ToDoRepository
//import com.projeku.myapplication.utils.UiState
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onStart
//import javax.inject.Inject
//
//class GetToDoListUseCase @Inject constructor(
//    private val repository: ToDoRepository
//) {
//    operator fun invoke(): Flow<UiState<List<ToDo>>> {
//        return repository.observeTodos()
//            .onStart {
//                if (repository.simulateOffline) {
//                    emit(UiState.Offline)
//                } else {
//                    emit(UiState.Loading)
//                }
//            }
//            .map { list ->
//                UiState.Success(list)
//            }
//            .catch { e ->
//                emit(UiState.Error(e.message ?: "Error"))
//            }
//        }
//    }
//}