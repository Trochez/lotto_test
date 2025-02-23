export default {
    template: `
        <div class="container">
            <h2>Seeder de Datos</h2>

            <p v-if="message" :class="messageType">{{ message }}</p>

            <button class="seeder-button" @click="seedCategories">Ejecutar Seeder de Categorías</button>
            <button class="seeder-button" @click="seedTransactions">Ejecutar Seeder de Transacciones</button>
        </div>
    `,
    data() {
        return {
            message: "",
            messageType: ""
        };
    },
    methods: {
        showMessage(text, type) {
            this.message = text;
            this.messageType = type;
            setTimeout(() => { this.message = ''; }, 5000);
        },
        seedCategories() {
            fetch("http://localhost:8081/category-seeder/seed")
                .then(response => {
                    if (!response.ok) throw new Error("Error al ejecutar Seeder de Categorías");
                    return response.json();
                })
                .then(() => {
                    this.showMessage("Seeder de Categorías ejecutado con éxito.", "success");
                })
                .catch(error => {
                    console.error("Error ejecutando Seeder de Categorías:", error);
                    this.showMessage("Error al ejecutar Seeder de Categorías.", "error");
                });
        },
        seedTransactions() {
            fetch("http://localhost:8082/transaction-seeder/seed")
                .then(response => {
                    if (!response.ok) throw new Error("Error al ejecutar Seeder de Transacciones");
                    return response.json();
                })
                .then(() => {
                    this.showMessage("Seeder de Transacciones ejecutado con éxito.", "success");
                })
                .catch(error => {
                    console.error("Error ejecutando Seeder de Transacciones:", error);
                    this.showMessage("Error al ejecutar Seeder de Transacciones.", "error");
                });
        }
    }
};
