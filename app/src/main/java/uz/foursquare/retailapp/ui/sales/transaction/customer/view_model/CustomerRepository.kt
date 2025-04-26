package uz.foursquare.retailapp.ui.sales.transaction.customer.view_model

import io.ktor.client.HttpClient
import uz.foursquare.retailapp.ui.sales.transaction.customer.type.CustomerType
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val client: HttpClient
) {
    private val customers = mutableListOf<CustomerType>(
        CustomerType(
            id = "1",
            name = "Asadbek",
            phoneNumber = "+998949357115",
            credit = 100000.0,
            orders = listOf("1", "2", "3")
        ),
        CustomerType(
            id = "2",
            name = "Bekzod",
            phoneNumber = "+998949357115",
            credit = 1200000.0,
            orders = listOf("1", "2", "3")
        ),
        CustomerType(
            id = "3",
            name = "Javohir",
            phoneNumber = "+998949357115",
            credit = 0.0,
            orders = listOf("1", "2", "3")
        ),
        CustomerType(
            id = "4",
            name = "Sherzod",
            phoneNumber = "+998949357115",
            credit = 150.0,
            orders = listOf("1", "2", "3")
        )
    )

    suspend fun addCustomer(customer: CustomerType): Result<Boolean> {
        customers.add(customer)

        return Result.success(true)
    }

    suspend fun searchCustomer(query: String): Result<List<CustomerType>> {
        val filteredCustomers = customers.filter { it.name.contains(query, ignoreCase = true) }
        customers.clear()
        customers.addAll(filteredCustomers)
        return Result.success(filteredCustomers)
    }

    suspend fun getCustomers(): Result<List<CustomerType>> {
        return Result.success(customers)
    }

    suspend fun editCustomer(customer: CustomerType): Result<Boolean> {
        return Result.success(true)
    }

    suspend fun archiveCustomer(customerId: String): Result<Boolean> {
        return Result.success(true)
    }

}