package com.mycompany.ringcard.services;

import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.dao.INotificacionDAO;
import com.mycompany.ringcard.utils.ConfigManager;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificadorCorteService {

    private final IMovimientoDAO movimientoDAO;
    private final INotificacionDAO notificacionDAO;
    private final EmailService emailService;
    private final EstadoCuentaService estadoCuentaService;
    private ScheduledExecutorService scheduler;

    public NotificadorCorteService(IMovimientoDAO movDAO, INotificacionDAO notifDAO) {
        this.movimientoDAO = movDAO;
        this.notificacionDAO = notifDAO;
        this.emailService = new EmailService();
        this.estadoCuentaService = new EstadoCuentaService(movDAO);
    }

    public void iniciarMonitoreoDiario(int idUsuarioLogueado, String correoUsuario) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Se ejecuta al instante, y luego cada 24 horas en segundo plano
        scheduler.scheduleAtFixedRate(() -> verificarCortes(idUsuarioLogueado, correoUsuario), 0, 24, TimeUnit.HOURS);
    }

    public void detenerMonitoreo() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void verificarCortes(int idUsuario, String correoUsuario) {
        LocalDate hoy = LocalDate.now();
        int diaActual = hoy.getDayOfMonth();
        int mesActual = hoy.getMonthValue();
        int anioActual = hoy.getYear();

        String rutaBase = ConfigManager.getRutaDocumentos();
        if (rutaBase == null) return; // Si no hay ruta configurada, no podemos adjuntar archivos

        try (ResultSet rsTarjetas = movimientoDAO.obtenerTarjetasDashboard(idUsuario)) {
            if (rsTarjetas != null) {
                while (rsTarjetas.next()) {
                    int idTarjeta = rsTarjetas.getInt("id_tarjeta");
                    String banco = rsTarjetas.getString("banco");
                    String tipo = rsTarjetas.getString("tipo").toLowerCase();
                    int diaCorte = rsTarjetas.getInt("fecha_corte"); // Para débito devolverá 0 en nuestra query
                    double saldoActual = rsTarjetas.getDouble("saldo_actual");
                    double abonado = rsTarjetas.getDouble("cantidadabonada");

                    boolean esDiaDeCorte = false;
                    String mensajeCorreo = "";

                    // Lógica de fechas
                    if (tipo.equals("credito") && diaActual == diaCorte) {
                        esDiaDeCorte = true;
                        double faltaPagar = saldoActual - abonado;
                        if (faltaPagar < 0) faltaPagar = 0;
                        mensajeCorreo = "Hola,\n\nHoy es la fecha de corte de tu tarjeta de crédito " + banco + ".\n"
                                      + "Tu deuda actual es de: $" + saldoActual + "\n"
                                      + "Te falta pagar: $" + faltaPagar + " para no generar intereses.\n\n"
                                      + "Adjunto encontrarás tu estado de cuenta de este mes.\n\nSaludos,\nRing-Card System";
                    } 
                    else if (tipo.equals("debito") && diaActual == 30) {
                        esDiaDeCorte = true;
                        mensajeCorreo = "Hola,\n\nTe enviamos el resumen mensual de tu tarjeta de débito " + banco + ".\n"
                                      + "Tu saldo actual es de: $" + saldoActual + "\n\n"
                                      + "Adjunto encontrarás el detalle de tus movimientos del mes.\n\nSaludos,\nRing-Card System";
                    }

                    // Si es día de corte y NO lo hemos notificado este mes
                    if (esDiaDeCorte && !notificacionDAO.yaSeNotificoEsteMes(idTarjeta, tipo, mesActual, anioActual)) {
                        
                        // 1. Forzamos la actualización del archivo DOCX
                        estadoCuentaService.actualizarEstadoCuenta(idTarjeta, banco, tipo);
                        
                        // 2. Buscamos el archivo generado
                        String mesAnio = String.format("%02d_%d", mesActual, anioActual);
                        String nombreArchivo = "EstadoCuenta_" + banco + "_" + tipo + "_" + mesAnio + ".docx";
                        String rutaCompleta = rutaBase + java.io.File.separator + nombreArchivo;

                        // 3. Enviamos el correo
                        String asunto = "Estado de Cuenta - " + banco.toUpperCase() + " (" + tipo.toUpperCase() + ")";
                        boolean enviado = emailService.enviarEstadoDeCuenta(correoUsuario, asunto, mensajeCorreo, rutaCompleta);

                        // 4. Registramos en la BD para que no se vuelva a enviar mañana o al reiniciar la app
                        if (enviado) {
                            notificacionDAO.registrarEstadoCuentaEnviado(idTarjeta, tipo, rutaCompleta, mesActual, anioActual);
                            notificacionDAO.registrarNotificacion(idUsuario, "Corte enviado por email: " + banco, "CORTE_" + tipo.toUpperCase());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en el demonio de cortes: " + e.getMessage());
        }
    }
}