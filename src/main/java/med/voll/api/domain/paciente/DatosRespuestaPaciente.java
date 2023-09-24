package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRespuestaPaciente(@NotNull Long id, String nombre, String email, String dni,
                                     String telefono, @Valid DatosDireccion direccion) {
}
