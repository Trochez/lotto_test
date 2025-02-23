import Transactions from "./components/transactions.js";
import Categories from "./components/categories.js";
import Seeder from "./components/seeder.js";

const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            currentView: "transactions"  // Carga Transacciones por defecto
        };
    },
    components: {
        transactions: Transactions,
        categories: Categories,
        seeder: Seeder
    }
});

app.mount("#app");
