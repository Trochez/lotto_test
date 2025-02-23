export default {
    template: `
        <div class="container">
            <h2>Gestión de Categorías</h2>

            <p v-if="message" :class="messageType">{{ message }}</p>

            <h3>Lista de Categorías</h3>
            <p v-if="categories.length === 0">No hay categorías disponibles.</p>
            <ul v-else>
                <li v-for="category in categories" :key="category.id">
                    {{ category.id }} - {{ category.name }}
                </li>
            </ul>
            <button @click="fetchCategories">Actualizar Categorías</button>

            <h3>Crear Categoría</h3>
            <input type="text" v-model="newCategory" placeholder="Nombre de categoría">
            <button @click="createCategory">Crear</button>

            <h3>Obtener Categoría por ID</h3>
            <input type="number" v-model="categoryId" placeholder="ID Categoría">
            <button @click="fetchCategoryById">Buscar</button>
            <p v-if="categoryResult">{{ categoryResult.name }}</p>
        </div>
    `,
    data() {
        return {
            categories: [],
            newCategory: '',
            categoryId: '',
            categoryResult: null,
            message: '',
            messageType: ''
        };
    },
    methods: {
        showMessage(text, type) {
            this.message = text;
            this.messageType = type;
            setTimeout(() => { this.message = ''; }, 5000);
        },
        fetchCategories() {
            fetch("http://localhost:8081/categories")
                .then(response => response.json())
                .then(data => {
                    this.categories = data;
                    this.showMessage("Categorías cargadas con éxito.", "success");
                })
                .catch(error => {
                    console.error("Error cargando categorías:", error);
                    this.showMessage("Error al cargar categorías.", "error");
                });
        },
        createCategory() {
            fetch("http://localhost:8081/categories", {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: this.newCategory })
            })
            .then(response => {
                if (!response.ok) throw new Error("Error al crear categoría");
                return response.json();
            })
            .then(() => {
                this.fetchCategories();
                this.showMessage("Categoría creada con éxito.", "success");
            })
            .catch(error => {
                console.error("Error creando categoría:", error);
                this.showMessage("Error al crear categoría.", "error");
            });
        },
        fetchCategoryById() {
            fetch(`http://localhost:8081/categories/${this.categoryId}`)
            .then(response => {
                if (!response.ok) throw new Error("Categoría no encontrada");
                return response.json();
            })
            .then(data => {
                this.categoryResult = data;
                this.showMessage("Categoría obtenida con éxito.", "success");
            })
            .catch(error => {
                console.error("Error obteniendo categoría:", error);
                this.showMessage("Error al obtener categoría.", "error");
            });
        }
    },
    mounted() {
        this.fetchCategories();
    }
};
