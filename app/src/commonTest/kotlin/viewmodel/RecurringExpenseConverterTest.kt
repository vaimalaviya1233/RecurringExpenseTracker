package viewmodel

import data.CurrencyValue
import data.Recurrence
import data.RecurringExpenseData
import kotlinx.datetime.Clock.System
import model.database.RecurrenceDatabase
import model.database.RecurringExpense
import ui.customizations.ExpenseColor
import kotlin.test.Test
import kotlin.test.assertEquals

class RecurringExpenseConverterTest {

    @Test
    fun `toFrontendType should convert backend type to frontend type with default currency code when no currency code is provided`() {
        val recurringExpense = RecurringExpense(
            id = 1,
            name = "Test Expense",
            description = "This is a test expense",
            price = 100.0f,
            everyXRecurrence = 2,
            recurrence = RecurrenceDatabase.Monthly.value,
            firstPayment = System.now().toEpochMilliseconds(),
            color = ExpenseColor.Green.toInt(),
            currencyCode = "USD"
        )

        val frontendType = recurringExpense.toFrontendType("USD")

        assertEquals(1, frontendType.id)
        assertEquals("Test Expense", frontendType.name)
        assertEquals("This is a test expense", frontendType.description)
        assertEquals(CurrencyValue(100.0f, "USD"), frontendType.price)
        assertEquals(CurrencyValue(50.0f, "USD"), frontendType.monthlyPrice) // 100 / 2
        assertEquals(Recurrence.Monthly, frontendType.recurrence)
        assertEquals(ExpenseColor.Green, frontendType.color)
    }

    @Test
    fun `toBackendType should convert frontend type to backend type with default currency code when no currency code is provided`() {
        val recurringExpenseData = RecurringExpenseData(
            id = 1,
            name = "Test Expense",
            description = "This is a test expense",
            price = CurrencyValue(100.0f, "USD"),
            monthlyPrice = CurrencyValue(50f, "USD"),
            everyXRecurrence = 2,
            recurrence = Recurrence.Monthly,
            firstPayment = System.now(),
            color = ExpenseColor.Green
        )

        val backendType = recurringExpenseData.toBackendType("USD")

        assertEquals(1, backendType.id)
        assertEquals("Test Expense", backendType.name)
        assertEquals("This is a test expense", backendType.description)
        assertEquals(100.0f, backendType.price)
        assertEquals(50.0f, backendType.getMonthlyPrice()) // 100 / 2
        assertEquals(RecurrenceDatabase.Monthly.value, backendType.recurrence)
        assertEquals(ExpenseColor.Green.toInt(), backendType.color)
    }
}
