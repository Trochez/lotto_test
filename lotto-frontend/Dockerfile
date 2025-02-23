# Usa una imagen de Apache para servir archivos estáticos
FROM httpd:2.4

# Copia todos los archivos del frontend al directorio de Apache
COPY . /usr/local/apache2/htdocs/

# Expone el puerto 80 para servir el frontend
EXPOSE 80
