// Страница управления вопросами: добавление, изменение, удаление, поиск.

const form = document.getElementById('qForm');
const fId = document.getElementById('qId');
const fText = document.getElementById('qText');
const fAnswer = document.getElementById('qAnswer');
const fCategory = document.getElementById('qCategory');
const saveBtn = document.getElementById('saveBtn');
const cancelBtn = document.getElementById('cancelBtn');
const formTitle = document.getElementById('formTitle');
const msg = document.getElementById('msg');
const tbody = document.getElementById('tbody');
const search = document.getElementById('search');

let items = [];
let limit = 50;

async function load() {
  try {
    const data = await api('/api/questions');
    items = data.items;
    limit = data.limit;
    render();
  } catch (e) {
    showMessage(msg, e.message, 'bad');
  }
}

function render() {
  const full = items.length >= limit;
  document.getElementById('storageText').textContent =
    `Записей: ${items.length} из ${limit}` + (full ? ' (хранилище заполнено)' : '');
  if (!fId.value) saveBtn.disabled = full;

  const cats = [...new Set(items.map(q => q.category))];
  document.getElementById('categoryList').innerHTML = cats.map(c => `<option value="${escapeHtml(c)}">`).join('');

  const q = search.value.trim().toLowerCase();
  const list = items.filter(x => !q || x.text.toLowerCase().includes(q) || x.answer.toLowerCase().includes(q));

  if (!list.length) {
    tbody.innerHTML = `<tr><td colspan="5">${items.length ? 'Ничего не найдено' : 'Вопросов нет'}</td></tr>`;
    return;
  }

  tbody.innerHTML = list.map(x => `
    <tr data-id="${x.id}">
      <td>${x.id}</td>
      <td>${escapeHtml(x.text)}</td>
      <td>${escapeHtml(x.answer)}</td>
      <td>${escapeHtml(x.category)}</td>
      <td>
        <button data-act="edit">Изменить</button>
        <button data-act="del">Удалить</button>
      </td>
    </tr>`).join('');
}

function resetForm() {
  form.reset();
  fId.value = '';
  formTitle.textContent = 'Новый вопрос';
  saveBtn.textContent = 'Добавить';
  cancelBtn.classList.add('hidden');
  fText.classList.remove('error');
  fAnswer.classList.remove('error');
  saveBtn.disabled = items.length >= limit;
}

function startEdit(id) {
  const q = items.find(x => x.id === id);
  if (!q) return;
  fId.value = q.id;
  fText.value = q.text;
  fAnswer.value = q.answer;
  fCategory.value = q.category;
  formTitle.textContent = `Изменение вопроса №${q.id}`;
  saveBtn.textContent = 'Сохранить';
  saveBtn.disabled = false;
  cancelBtn.classList.remove('hidden');
  msg.textContent = '';
  window.scrollTo(0, 0);
  fText.focus();
}

form.addEventListener('submit', async e => {
  e.preventDefault();
  const body = { text: fText.value.trim(), answer: fAnswer.value.trim(), category: fCategory.value.trim() };

  const textOk = body.text.length >= 3;
  const answerOk = body.answer.length > 0;
  fText.classList.toggle('error', !textOk);
  fAnswer.classList.toggle('error', !answerOk);
  if (!textOk || !answerOk) {
    return showMessage(msg, 'Введите вопрос (не короче 3 символов) и ответ.', 'bad');
  }

  try {
    if (fId.value) {
      await api(`/api/questions/${fId.value}`, { method: 'PUT', body });
      showMessage(msg, 'Вопрос сохранён.');
    } else {
      await api('/api/questions', { method: 'POST', body });
      showMessage(msg, 'Вопрос добавлен.');
    }
    resetForm();
    load();
  } catch (err) {
    showMessage(msg, err.message, 'bad');
  }
});

cancelBtn.onclick = () => {
  resetForm();
  msg.textContent = '';
};

tbody.addEventListener('click', async e => {
  const btn = e.target.closest('button[data-act]');
  if (!btn) return;
  const id = Number(btn.closest('tr').dataset.id);
  if (btn.dataset.act === 'edit') return startEdit(id);

  const q = items.find(x => x.id === id);
  if (!confirm(`Удалить вопрос «${q.text}»?`)) return;
  try {
    await api(`/api/questions/${id}`, { method: 'DELETE' });
    if (Number(fId.value) === id) resetForm();
    showMessage(msg, 'Вопрос удалён.');
    load();
  } catch (err) {
    showMessage(msg, err.message, 'bad');
  }
});

search.addEventListener('input', render);

load();
