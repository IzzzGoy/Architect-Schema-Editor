package ru.alexey.ndimmatrix.generator.presentation.impl

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import ru.alexey.ndimmatrix.generator.presentation.api.chains.CreateParamEventChain
import ru.alexey.ndimmatrix.generator.presentation.api.chains.CreateParameterModelChain
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.CreateParameterNameHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

@ComponentScan("ru.alexey.ndimmatrix.generator.presentation.impl")
@Module
class PresentationDIModule {

    @Factory
    fun provideSetParameterNameChain(
        parameterHolder: CreateParameterHolder,
        createParameterNameHandler: CreateParameterNameHandler,
    ): CreateParameterModelChain {
        return CreateParameterModelChain(parameterHolder, createParameterNameHandler)
    }

    @Factory
    fun provideCreateParamEventChain(
        parameterHolder: CreateParameterHolder,
        parametersHolder: ParametersHolder,
        createParameterNameHandler: CreateParameterNameHandler,
    ): CreateParamEventChain {
        return CreateParamEventChain(
            createParameterNameHandler = createParameterNameHandler,
            parameterHolder = parameterHolder,
            parametersHolder = parametersHolder,
        )
    }

}