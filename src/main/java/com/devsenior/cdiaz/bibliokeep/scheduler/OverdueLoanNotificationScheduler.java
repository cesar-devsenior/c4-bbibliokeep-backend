package com.devsenior.cdiaz.bibliokeep.scheduler;

import java.time.LocalDate;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.devsenior.cdiaz.bibliokeep.repository.LoanRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OverdueLoanNotificationScheduler {

    private final LoanRepository loanRepository;
    private final JavaMailSender mailSender;

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional(readOnly = true)
    public void sendOverdueLoanNotifications() {
        var overdueLoans = loanRepository.findByDueDateBeforeAndReturnedFalse(LocalDate.now());

        overdueLoans.forEach(loan -> {
            var owner = loan.getBook().getOwner();
            if (owner == null || owner.getEmail() == null || owner.getEmail().isBlank()) {
                return;
            }

            var message = new SimpleMailMessage();
            message.setTo(owner.getEmail());
            message.setSubject("Préstamo vencido: " + loan.getBook().getTitle());
            message.setText("Hola,\n\n" +
                    "Tu libro prestado '" + loan.getBook().getTitle() + "' para " + loan.getContactName() + " " +
                    "vence el " + loan.getDueDate() + ". Por favor, recopílelo o marque como devuelto si ya fue retornado.\n\n" +
                    "Gracias,\n" +
                    "BiblioKeep");

            mailSender.send(message);
        });
    }
}
