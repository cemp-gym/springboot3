package med.voll.api.domain.paciente;

// DTO (Data Transfer Object)
public record DatosListadoPaciente(Long id, String nombre, String dni, String email) {

    public DatosListadoPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNombre(), paciente.getDni(), paciente.getEmail());
    }

}
