package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.desafio.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadorDeConsultas;

    @Autowired
    List<ValidadorCancelamientoDeConsulta> validadoresCancelamiento;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datosAgendarConsulta) {

        if (!pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }

        if (datosAgendarConsulta.idMedico() != null && !medicoRepository.existsById(datosAgendarConsulta.idMedico())) {
            throw new ValidacionDeIntegridad("Este id para el médico no fue encontrado");
        }

        validadorDeConsultas.forEach(v -> v.validar(datosAgendarConsulta));

        var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

        var medico = seleccionarMedico(datosAgendarConsulta);

        if (medico == null) {
            throw new ValidacionDeIntegridad("No existen médicos disponibles para este horario y especialidad.");
        }

        var consulta = new Consulta(medico, paciente, datosAgendarConsulta.fecha());
        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelamientoConsulta datosCancelamientoConsulta) {
        if (!consultaRepository.existsById(datosCancelamientoConsulta.idConsulta())) {
            throw new ValidacionDeIntegridad("No se ha programado la consulta. Id de la consulta no existe.");
        }
        validadoresCancelamiento.forEach(v -> v.validar(datosCancelamientoConsulta));

        var consulta = consultaRepository.getReferenceById(datosCancelamientoConsulta.idConsulta());
        consulta.cancelar(datosCancelamientoConsulta.motivoCancelamiento());
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
        if (datosAgendarConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
        }
        if (datosAgendarConsulta.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el médico.");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(), datosAgendarConsulta.fecha());
    }

}
