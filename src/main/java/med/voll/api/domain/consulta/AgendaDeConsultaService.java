package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public void agendar(DatosAgendarConsulta datosAgendarConsulta) {
        if (pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }

        if (datosAgendarConsulta.idMedico() != null && medicoRepository.existsById(datosAgendarConsulta.idMedico())) {
            throw new ValidacionDeIntegridad("Este id para el médico no fue encontrado");
        }

        var medico = seleccionarMedico(datosAgendarConsulta);

        var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

        var consulta = new Consulta(null, medico, paciente, datosAgendarConsulta.fecha());
        consultaRepository.save(consulta);
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
