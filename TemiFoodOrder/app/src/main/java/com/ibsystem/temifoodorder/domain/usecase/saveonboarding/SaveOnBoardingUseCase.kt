package com.ibsystem.temifoodorder.domain.usecase.saveonboarding

import com.ibsystem.temifoodorder.data.repository.Repository

class SaveOnBoardingUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(isCompleted: Boolean) {
        repository.saveOnBoardingState(isCompleted = isCompleted)
    }

}