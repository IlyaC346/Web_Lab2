// Страница контактов: статус «открыто/закрыто» и форма обратной связи.

const now = new Date();
const isOpen = now.getDay() >= 1 && now.getDay() <= 5 && now.getHours() >= 9 && now.getHours() < 18;
document.getElementById('openStatus').textContent = isOpen ? '(сейчас открыто)' : '(сейчас закрыто)';

const fbForm = document.getElementById('fbForm');
const fbName = document.getElementById('fbName');
const fbEmail = document.getElementById('fbEmail');
const fbMsg = document.getElementById('fbMsg');
const fbResult = document.getElementById('fbResult');

[fbName, fbEmail, fbMsg].forEach(el => el.addEventListener('input', () => el.classList.remove('error')));

fbForm.addEventListener('submit', async e => {
  e.preventDefault();
  const body = { name: fbName.value.trim(), email: fbEmail.value.trim(), message: fbMsg.value.trim() };
  const checks = [
    [fbName, body.name.length > 0, 'Введите имя.'],
    [fbEmail, /^\S+@\S+\.\S+$/.test(body.email), 'Введите правильный e-mail.'],
    [fbMsg, body.message.length >= 5, 'Сообщение должно быть не короче 5 символов.'],
  ];
  const failed = checks.filter(([el, ok]) => {
    el.classList.toggle('error', !ok);
    return !ok;
  });
  if (failed.length) return showMessage(fbResult, failed[0][2], 'bad');

  try {
    await api('/api/feedback', { method: 'POST', body });
    showMessage(fbResult, `Спасибо, ${body.name}, сообщение отправлено.`);
    fbForm.reset();
  } catch (err) {
    showMessage(fbResult, err.message, 'bad');
  }
});
