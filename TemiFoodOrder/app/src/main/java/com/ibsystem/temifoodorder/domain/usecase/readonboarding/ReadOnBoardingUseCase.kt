package com.ibsystem.temifoodorder.domain.usecase.readonboarding

import com.ibsystem.temifoodorder.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class ReadOnBoardingUseCase(private val repository: Repository) {

    operator fun invoke(): Flow<Boolean> = repository.readOnBoardingState()

}