package co.bluerebhirt.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroForm {
    @NotBlank(message = "Escribe tu nombre.") @Size(max = 80) private String nombre;
    @NotBlank(message = "Escribe tu correo.") @Email(message = "Escribe un correo válido.") @Size(max = 120) private String correo;
    @NotBlank(message = "Escribe una contraseña.") @Size(min = 6, max = 72, message = "Mínimo 6 caracteres y máximo 72.") private String clave;
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}
