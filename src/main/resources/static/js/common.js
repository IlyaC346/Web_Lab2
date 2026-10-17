// Общие функции: меню, подвал, вывод сообщений, запросы к серверу.

const PAGES = [
  { href: 'index.html', title: 'Главная' },
  { href: 'manage.html', title: 'Вопросы' },
  { href: 'quiz.html', title: 'Викторина' },
  { href: 'contacts.html', title: 'Контакты' },
];

const SERVER_URL = 'http://localhost:8080';

function renderLayout() {
  const current = location.pathname.split('/').pop() || 'index.html';

  const header = document.createElement('header');
  header.innerHTML = '<nav>' + PAGES.map(p =>
    `<a href="${p.href}" class="${p.href === current ? 'active' : ''}">${p.title}</a>`).join('') + '</nav>';
  document.body.prepend(header);

  const footer = document.createElement('footer');
  footer.textContent = `ООО «КвизПро», ${new Date().getFullYear()}`;
  document.body.append(footer);

  if (location.protocol === 'file:') showServerWarning();
}

// Предупреждение, если страница открыта не через сервер или сервер не запущен
function showServerWarning() {
  if (document.getElementById('serverWarning')) return;
  const warning = document.createElement('div');
  warning.id = 'serverWarning';
  warning.className = 'warning';
  warning.innerHTML = `Нет связи с сервером. Запустите приложение (класс QuizApplication) ` +
    `и откройте сайт по адресу <a href="${SERVER_URL}">${SERVER_URL}</a>.`;
  document.querySelector('main').prepend(warning);
}

// Показать сообщение в элементе el; type: 'ok' или 'bad'
function showMessage(el, text, type = 'ok') {
  el.textContent = text;
  el.className = 'message ' + type;
}

async function api(url, options = {}) {
  let res;
  try {
    res = await fetch(url.replace(/^\//, ''), {
      headers: { 'Content-Type': 'application/json' },
      ...options,
      body: options.body ? JSON.stringify(options.body) : undefined,
    });
  } catch {
    showServerWarning();
    throw new Error('Нет связи с сервером');
  }

  // Ответ не в JSON — значит, страницу отдаёт не наш сервер
  if (!(res.headers.get('Content-Type') || '').includes('application/json')) {
    showServerWarning();
    throw new Error('Нет связи с сервером');
  }

  const data = await res.json();
  if (!res.ok) throw new Error(data.error || `Ошибка ${res.status}`);
  return data;
}

function escapeHtml(s) {
  return String(s).replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

renderLayout();
