package club.tempvs.user

import org.springframework.scheduling.annotation.Scheduled

class EmailVerificationCleanupJob {

    @Scheduled(initialDelay = 300000L, fixedRate = 3600000L)
    def execute() {
        EmailVerification.executeUpdate(
                "delete EmailVerification where date_created < :dateToDelete ", [dateToDelete: new Date() -1])
    }
}
