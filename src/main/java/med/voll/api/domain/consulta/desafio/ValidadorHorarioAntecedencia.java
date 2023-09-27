package med.voll.api.domain.consulta.desafio;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaCancelamiento")
public class ValidadorHorarioAntecedencia implements ValidadorCancelamientoDeConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    //    @Override
    public void validar(DatosCancelamientoConsulta datosCancelamientoConsulta) {
        var consulta = consultaRepository.getReferenceById(datosCancelamientoConsulta.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if (diferenciaEnHoras < 24) {
            throw new ValidationException("La consulta solo puede ser cancelada con una antecedencia mínima de 24 horas.");
        }
    }
}
