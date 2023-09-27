package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findByActivoTrue(Pageable paginacion);

    // Primer cambio en Spring Boot 3: documentar, probar y preparar una API para su implementación
    @Query("""
            SELECT m FROM Medico m 
            WHERE 
            m.activo=1 
            AND 
            m.especialidad=:especialidad 
            AND 
            m.id NOT IN(
                SELECT c.medico.id FROM Consulta c
                WHERE
                c.fecha=:fecha
            ) 
            ORDER BY RAND()
            LIMIT 1
            """)
    Medico seleccionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);

    @Query("""
            SELECT m.activo FROM Medico m 
            WHERE 
            m.id=:idMedico
            """)
    Boolean findActivoById(Long idMedico);
}
