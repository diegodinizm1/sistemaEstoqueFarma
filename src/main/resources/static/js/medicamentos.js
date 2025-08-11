document.addEventListener('DOMContentLoaded', function() {
    // --- Seletores de Elementos ---
    const tabelaCorpo = document.getElementById('corpo-tabela-medicamentos');
    const modalElement = document.getElementById('medicamentoModal');
    const medicamentoModal = new bootstrap.Modal(modalElement);
    const formMedicamento = document.getElementById('form-medicamento');
    const btnSalvar = document.getElementById('btn-salvar-medicamento');
    const modalTitle = document.querySelector('#medicamentoModal .modal-title');
    const nomeMedicamentoInput = document.getElementById('nomeMedicamento');
    const modalAlertPlaceholder = document.querySelector('#medicamentoModal #modal-alert-placeholder');

    let editandoId = null; // Variável para controlar se estamos a criar ou a editar

    // --- Funções ---

    // Função para carregar e exibir os medicamentos na tabela
    const carregarMedicamentos = async () => {
        try {
            const response = await fetch('/medicamentos');
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao carregar medicamentos.');

            const medicamentos = await response.json();
            tabelaCorpo.innerHTML = '';

            if (medicamentos.length === 0) {
                tabelaCorpo.innerHTML = '<tr><td colspan="3" class="text-center p-4 text-muted">Nenhum medicamento cadastrado.</td></tr>';
                return;
            }

            medicamentos.forEach(med => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${med.id}</td>
                    <td>${med.nome}</td>
                    <td class="text-end">
                        <button class="btn btn-sm btn-outline-primary me-2 btn-editar" data-id="${med.id}" data-nome="${med.nome}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-excluir" data-id="${med.id}">Excluir</button>
                    </td>
                `;
                tabelaCorpo.appendChild(tr);
            });
        } catch (error) {
            console.error('Erro:', error);
            tabelaCorpo.innerHTML = `<tr><td colspan="3" class="text-center text-danger p-4">${error.message}</td></tr>`;
        }
    };

    // Função para excluir um medicamento
    const excluirMedicamento = async (id) => {
        try {
            const response = await fetch(`/medicamentos/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao excluir.');
            }
            carregarMedicamentos(); // Recarrega a lista após o sucesso
        } catch (error) {
            console.error('Erro ao excluir:', error);
            alert(`Erro: ${error.message}`);
        }
    };

    // --- Event Listeners (Ouvintes de Eventos) ---

    // Abre o modal em modo de CRIAÇÃO
    modalElement.addEventListener('show.bs.modal', () => {
        if (!editandoId) { // Só limpa se não estiver a editar
            modalTitle.textContent = 'Novo Medicamento';
            formMedicamento.reset();
            modalAlertPlaceholder.innerHTML = '';
        }
    });

    // Limpa o ID de edição quando o modal fecha
    modalElement.addEventListener('hidden.bs.modal', () => {
        editandoId = null;
    });

    // Delegação de eventos: ouve cliques na tabela inteira
    tabelaCorpo.addEventListener('click', (event) => {
        const target = event.target;

        // Se o botão clicado for o de EDITAR
        if (target.classList.contains('btn-editar')) {
            editandoId = target.dataset.id;
            const nome = target.dataset.nome;

            modalTitle.textContent = `Editando Medicamento #${editandoId}`;
            nomeMedicamentoInput.value = nome;
            modalAlertPlaceholder.innerHTML = '';
            medicamentoModal.show();
        }

        // Se o botão clicado for o de EXCLUIR
        if (target.classList.contains('btn-excluir')) {
            const id = target.dataset.id;
            if (confirm(`Tem a certeza que deseja excluir o medicamento ID #${id}?`)) {
                excluirMedicamento(id);
            }
        }
    });

    // Lida com o clique no botão Salvar (aciona o submit do formulário)
    btnSalvar.addEventListener('click', () => formMedicamento.requestSubmit());

    // Lida com o envio do formulário (criação ou edição)
    formMedicamento.addEventListener('submit', async (event) => {
        event.preventDefault();
        modalAlertPlaceholder.innerHTML = '';
        const nome = nomeMedicamentoInput.value;

        const url = editandoId ? `/medicamentos/${editandoId}` : '/medicamentos';
        const method = editandoId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao salvar.');
            }

            medicamentoModal.hide();
            carregarMedicamentos();
        } catch (error) {
            console.error('Erro ao salvar:', error);
            modalAlertPlaceholder.innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
        }
    });

    // --- Inicialização ---
    carregarMedicamentos();
});
