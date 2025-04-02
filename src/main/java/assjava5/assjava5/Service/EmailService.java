package assjava5.assjava5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
@Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(String toEmail, String activationCode) {
        String subject = "Kích hoạt tài khoản";
        String body = "Nhấp vào liên kết bên dưới để kích hoạt tài khoản của bạn:\n"
                + "http://localhost:8080/activate?code=" + activationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    public void sendNewPasswordEmail(String toEmail, String newPassword) {
        String subject = "Mật khẩu mới của bạn";
        String body = "Mật khẩu mới của bạn là: " + newPassword
                + "\nVui lòng đăng nhập và thay đổi mật khẩu ngay lập tức để đảm bảo an toàn.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
