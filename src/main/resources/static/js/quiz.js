// Страница викторины: случайные вопросы, ввод и проверка ответов.

const $ = id => document.getElementById(id);
const setup = $('setup'), play = $('play'), result = $('result');
const answerInput = $('answer'), feedback = $('feedback');

let quiz = [];
let index = 0;
let score = 0;
let answered = false;
let busy = false;
let history = [];

async function loadCount() {
  try {
    const data = await api('/api/questions');
    $('available').textContent = `Всего вопросов в базе: ${data.count}`;
    $('count').max = Math.max(1, data.count);
    $('startBtn').disabled = data.count === 0;
  } catch (e) {
    $('available').textContent = e.message;
  }
}

async function start() {
  const count = Number($('count').value) || 5;
  try {
    quiz = (await api('/api/questions/random?count=' + count)).items;
  } catch (e) {
    return showMessage(feedback, e.message, 'bad');
  }
  if (!quiz.length) return;

  index = 0;
  score = 0;
  history = [];
  setup.classList.add('hidden');
  result.classList.add('hidden');
  play.classList.remove('hidden');
  showQuestion();
}

function showQuestion() {
  const q = quiz[index];
  answered = false;
  $('counterText').textContent = `Вопрос ${index + 1} из ${quiz.length}`;
  $('qText').textContent = q.text;
  answerInput.value = '';
  answerInput.disabled = false;
  answerInput.classList.remove('error');
  feedback.textContent = '';
  $('checkBtn').classList.remove('hidden');
  $('nextBtn').classList.add('hidden');
  answerInput.focus();
}

async function check() {
  if (answered || busy) return;
  const q = quiz[index];
  const userAnswer = answerInput.value.trim();
  if (!userAnswer) {
    answerInput.classList.add('error');
    return showMessage(feedback, 'Введите ответ.', 'bad');
  }

  let res;
  busy = true;
  try {
    res = await api(`/api/questions/${q.id}/check`, { method: 'POST', body: { answer: userAnswer } });
  } catch (e) {
    return showMessage(feedback, e.message, 'bad');
  } finally {
    busy = false;
  }

  answered = true;
  if (res.correct) score++;
  history.push({ text: q.text, userAnswer, correctAnswer: res.correctAnswer, correct: res.correct });

  if (res.correct) showMessage(feedback, 'Правильно.');
  else showMessage(feedback, `Неправильно. Правильный ответ: ${res.correctAnswer}`, 'bad');

  answerInput.disabled = true;
  $('checkBtn').classList.add('hidden');
  $('nextBtn').classList.remove('hidden');
  $('nextBtn').textContent = index + 1 < quiz.length ? 'Следующий вопрос' : 'Показать результат';
  $('nextBtn').focus();
}

function next() {
  if (!answered) return;
  index++;
  if (index < quiz.length) showQuestion();
  else finish();
}

function finish() {
  play.classList.add('hidden');
  result.classList.remove('hidden');
  $('resultText').textContent = `Правильных ответов: ${score} из ${history.length}.`;
  $('resultBody').innerHTML = history.map(h => `
    <tr>
      <td>${escapeHtml(h.text)}</td>
      <td class="${h.correct ? 'ok' : 'bad'}">${escapeHtml(h.userAnswer)}</td>
      <td>${escapeHtml(h.correctAnswer)}</td>
    </tr>`).join('') || '<tr><td colspan="3">Нет ответов</td></tr>';
}

$('startBtn').onclick = start;
$('againBtn').onclick = start;
$('answerForm').addEventListener('submit', e => { e.preventDefault(); check(); });
$('nextBtn').onclick = next;
$('stopBtn').onclick = () => {
  if (confirm('Закончить викторину?')) finish();
};
answerInput.addEventListener('input', () => answerInput.classList.remove('error'));

loadCount();
