export default {
    template: `
        <div class="container">
            <h2>Gestión de Transacciones</h2>

            <p v-if="message" :class="messageType">{{ message }}</p>

            <h3>Lista de Transacciones</h3>
            <p v-if="transactions.length === 0" class="empty-list">No hay transacciones disponibles.</p>
            <ul v-else class="transaction-list">
                <li v-for="transaction in transactions" 
                    :key="transaction.id"
                    @click="selectTransaction(transaction)"
                    :class="{ 'selected': selectedTransaction && selectedTransaction.id === transaction.id }">
                    <span class="transaction-amount">$ {{ transaction.amount.toFixed(2) }}</span>
                    <span class="transaction-category">{{ getCategoryName(transaction.categoryId) }}</span>
                    <span class="transaction-status">{{ transaction.status }}</span>
                </li>
            </ul>
            <button @click="fetchTransactions">Actualizar Transacciones</button>
            
            <h3>Crear Transacción</h3>
            <input type="number" v-model="newTransaction.card_number" placeholder="Número de tarjeta">
            <input type="number" v-model="newTransaction.amount" placeholder="Monto">
            <input type="date" v-model="newTransaction.date">
            <select v-model="newTransaction.status">
                <option value="PENDIENTE">PENDIENTE</option>
                <option value="APROBADA">APROBADA</option>
                <option value="RECHAZADA">RECHAZADA</option>
            </select>
            <select v-model="newTransaction.categoryId">
                <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                </option>
            </select>
            <button @click="createTransaction">Crear</button>

            <h3 v-if="selectedTransaction">Actualizar Estado</h3>
            <div v-if="selectedTransaction">
                <p><strong>Transacción Seleccionada:</strong> #{{ selectedTransaction.id }}</p>
                <select v-model="selectedTransaction.status">
                    <option value="PENDIENTE">PENDIENTE</option>
                    <option value="APROBADA">APROBADA</option>
                    <option value="RECHAZADA">RECHAZADA</option>
                </select>
                <button @click="updateTransactionStatus">Actualizar Estado</button>
            </div>

            <h3>Filtrar Transacciones</h3>
            <select v-model="filterStatus">
                <option value="">Selecciona un estado</option>
                <option value="PENDIENTE">PENDIENTE</option>
                <option value="APROBADA">APROBADA</option>
                <option value="RECHAZADA">RECHAZADA</option>
            </select>
            <button @click="fetchByStatus">Filtrar por Estado</button>

            <select v-model="selectedCategory">
                <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                </option>
            </select>
            <button @click="fetchByCategory">Filtrar por Categoría</button>

            <input type="date" v-model="startDate">
            <input type="date" v-model="endDate">
            <button @click="fetchByDate">Filtrar por Fecha</button>

            <h3>Resumen de Gastos por Categoría</h3>
            <button @click="fetchSummary">Ver Resumen</button>
            <ul v-if="Object.keys(summary).length">
                <li v-for="(amount, category) in summary" :key="category">
                    <strong>Categoría: {{ category }}</strong> - Total: {{ amount.toFixed(2) }}
                </li>
            </ul>
        </div>
    `,
    data() {
        return {
            transactions: [],
            categories: [],
            summary: {},
            newTransaction: { card_number: '', amount: '', date: '', status: 'PENDIENTE', categoryId: '' },
            selectedTransaction: null,
            selectedStatus: '',
            filterStatus: '',
            selectedCategory: '',
            startDate: '',
            endDate: '',
            message: '',
            messageType: ''
        };
    },
    methods: {
        showMessage(text, type) {
            this.message = text;
            this.messageType = type;
            setTimeout(() => { this.message = ''; }, 5000); // Ocultar mensaje después de 5 segundos
        },
        fetchTransactions() {
            fetch("http://localhost:8082/transactions")
                .then(response => response.json())
                .then(data => {
                    this.transactions = data.length ? data : [];
                    if (data.length === 0) {
                        this.showMessage("No hay transacciones disponibles.", "error");
                    } else {
                        this.showMessage("Transacciones cargadas con éxito.", "success");
                    }
                })
                .catch(error => {
                    console.error("Error cargando transacciones:", error);
                    this.showMessage("Error al cargar transacciones.", "error");
                });
        },
        createTransaction() {
            fetch("http://localhost:8082/transactions", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(this.newTransaction)
            })
            .then(response => {
                if (!response.ok) throw new Error("Error al crear transacción");
                return response.json();
            })
            .then(() => {
                this.fetchTransactions();
                this.showMessage("Transacción creada con éxito.", "success");
            })
            .catch(error => {
                console.error("Error creando transacción:", error);
                this.showMessage("Error al crear transacción.", "error");
            });
        },
        selectTransaction(transaction) {
            this.selectedTransaction = { ...transaction }; // Clonar para evitar cambios en la lista
        },
        updateTransactionStatus() {
            if (!this.selectedTransaction) return;
            fetch(`http://localhost:8082/transactions/${this.selectedTransaction}/status?status=${this.selectedStatus}`, { method: "PUT" })
            .then(() => {
                this.fetchTransactions();
                this.showMessage("Estado actualizado con éxito.", "success");
            })
            .catch(error => {
                console.error("Error actualizando estado:", error);
                this.showMessage("Error al actualizar estado.", "error");
            });
        },
        fetchByStatus() {
            fetch(`http://localhost:8082/transactions/status/${this.filterStatus}`)
            .then(response => response.json())
            .then(data => {
                this.transactions = data.length ? data : [];
                if (data.length === 0) {
                    this.showMessage("No hay transacciones con este estado.", "error");
                } else {
                    this.showMessage("Transacciones filtradas por estado.", "success");
                }
            })
            .catch(error => {
                console.error("Error obteniendo transacciones:", error);
                this.showMessage("Error al filtrar transacciones.", "error");
            });
        },
        fetchByCategory() {
            fetch(`http://localhost:8082/transactions/category/${this.selectedCategory}`)
            .then(response => response.json())
            .then(data => {
                this.transactions = data.length ? data : [];
                if (data.length === 0) {
                    this.showMessage("No hay transacciones en esta categoría.", "error");
                } else {
                    this.showMessage("Transacciones filtradas por categoría.", "success");
                }
            })
            .catch(error => {
                console.error("Error obteniendo transacciones:", error);
                this.showMessage("Error al filtrar por categoría.", "error");
            });
        },
        fetchByDate() {
            fetch(`http://localhost:8082/transactions/date-range?startDate=${this.startDate}&endDate=${this.endDate}`)
            .then(response => response.json())
            .then(data => {
                this.transactions = data.length ? data : [];
                if (data.length === 0) {
                    this.showMessage("No hay transacciones en este rango de fechas.", "error");
                } else {
                    this.showMessage("Transacciones filtradas por fecha.", "success");
                }
            })
            .catch(error => {
                console.error("Error obteniendo transacciones:", error);
                this.showMessage("Error al filtrar por fecha.", "error");
            });
        },
        fetchSummary() {
            fetch("http://localhost:8082/transactions/summary-by-category")
            .then(response => response.json())
            .then(data => {
                this.summary = data;
                this.showMessage("Resumen de gastos cargado con éxito.", "success");
            })
            .catch(error => {
                console.error("Error obteniendo resumen:", error);
                this.showMessage("Error al cargar resumen.", "error");
            });
        },
        fetchCategories() {
            fetch("http://localhost:8081/categories")
            .then(response => response.json())
            .then(data => {
                this.categories = data;
                this.categories = data.length ? data : [];
            })
            .catch(error => console.error("Error obteniendo categorías:", error));
        },
        getCategoryName(categoryId) {
            const category = this.categories.find(cat => cat.id === categoryId);
            return category ? category.name : "Categoría desconocida";
        }
    },
    mounted() {
        this.fetchTransactions();
        this.fetchCategories();
    }
};
