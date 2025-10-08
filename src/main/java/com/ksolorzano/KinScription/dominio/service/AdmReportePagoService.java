package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmReportePagoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.AdmReportePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdmReportePagoService {

    private final AdmReportePagoRepository reportePagoRepository;

    @Autowired
    public AdmReportePagoService(AdmReportePagoRepository reportePagoRepository) {
        this.reportePagoRepository = reportePagoRepository;
    }

    /**
     * Genera el reporte de inscripción y 10 cuotas mensuales para un participante.
     * @param participante El participante para quien se generan los pagos.
     * @param montoInscripcion El costo de la inscripción.
     * @param montoMensualidad El costo de la cuota mensual.
     */
    public void generarReporteDePagos(AdmParticipante participante, BigDecimal montoInscripcion, BigDecimal montoMensualidad) {
        List<AdmReportePago> pagos = new ArrayList<>();

        // Pago de Inscripción
        AdmReportePago inscripcion = new AdmReportePago();
        inscripcion.setParticipante(participante);
        inscripcion.setConcepto("Inscripción");
        inscripcion.setMonto(montoInscripcion);
        inscripcion.setFechaGeneracion(LocalDate.now());
        pagos.add(inscripcion);

        // 10 Cuotas Mensuales
        for (int i = 1; i <= 10; i++) {
            AdmReportePago mensualidad = new AdmReportePago();
            mensualidad.setParticipante(participante);
            mensualidad.setConcepto("Cuota Mensual " + i + "/10");
            mensualidad.setMonto(montoMensualidad);
            mensualidad.setFechaGeneracion(LocalDate.now());
            pagos.add(mensualidad);
        }

        reportePagoRepository.saveAll(pagos);
    }

    public List<AdmReportePago> getAll() {
        return reportePagoRepository.findAll();
    }

    public Optional<AdmReportePago> getById(Integer id) {
        return reportePagoRepository.findById(id);
    }

    @Transactional
    public Optional<AdmReportePago> update(Integer id, AdmReportePago newData) {
        return reportePagoRepository.findById(id).map(pago -> {
            pago.setConcepto(newData.getConcepto());
            pago.setMonto(newData.getMonto());
            pago.setFechaGeneracion(newData.getFechaGeneracion());
            return reportePagoRepository.save(pago);
        });
    }

    @Transactional
    public boolean delete(Integer id) {
        return getById(id).map(pago -> {
            reportePagoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public AdmReportePago save(AdmReportePago pago) {
        return reportePagoRepository.save(pago);
    }
}