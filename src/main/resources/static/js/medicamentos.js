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
    const filtroStatusSelect = document.getElementById('filtroStatusMedicamento');
    const descricaoDetalhadaInput = document.getElementById('discriminacaoMed');
    const unidadeMedidaInput = document.getElementById('unidadeMedida');

    let editandoId = null;

    const carregarMedicamentos = async () => {
        const status = filtroStatusSelect.value;
        let url = '';

        if (status === 'ativos') {
            url = '/medicamentos/ativos';
        } else if (status === 'inativos') {
            url = '/medicamentos/inativos';
        } else {
            url = '/medicamentos';
        }

        try {
            const response = await fetch(url);
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao carregar medicamentos.');

            const medicamentos = await response.json();
            tabelaCorpo.innerHTML = '';

            if (medicamentos.length === 0) {
                tabelaCorpo.innerHTML = '<tr><td colspan="3" class="text-center p-4 text-muted">Nenhum medicamento encontrado.</td></tr>';
                return;
            }

            medicamentos.forEach(med => {
                const tr = document.createElement('tr');
                let botoesAcao = '';

                if (med.ativo) {
                    botoesAcao = `
                        <button class="btn btn-sm btn-outline-primary me-2 btn-editar" data-id="${med.id}" data-nome="${med.nome}" data-descricao-detalhada="${med.descricaoDetalhada}"
                            data-unidade-medida="${med.unidadeMedida}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-inativar" data-id="${med.id}">Inativar</button>
                    `;
                } else {
                    botoesAcao = `
                        <span class="text-muted fst-italic me-2">Inativo</span>
                        <button class="btn btn-sm btn-outline-success btn-ativar" data-id="${med.id}">Ativar</button>
                    `;
                }

                tr.innerHTML = `
                    <td>${med.id}</td>
                    <td>${med.nome}</td>
                    <td>${med.descricaoDetalhada}</td>
                    <td>${med.unidadeMedida}</td>
                    <td class="text-end">${botoesAcao}</td>
                `;
                tabelaCorpo.appendChild(tr);
            });
        } catch (error) {
            console.error('Erro:', error);
            tabelaCorpo.innerHTML = `<tr><td colspan="3" class="text-center text-danger p-4">${error.message}</td></tr>`;
        }
    };

    const inativarMedicamento = async (id) => {
        try {
            const response = await fetch(`/medicamentos/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao inativar.');
            }
            carregarMedicamentos();
        } catch (error) {
            console.error('Erro ao inativar:', error);
            alert(`Erro: ${error.message}`);
        }
    };

    const ativarMedicamento = async (id) => {
        try {
            const response = await fetch(`/medicamentos/${id}/ativar`, { method: 'PATCH' });
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao ativar.');
            }
            carregarMedicamentos();
        } catch (error) {
            error('Erro ao ativar:', error);
            alert(`Erro: ${error.message}`);
        }
    };

    modalElement.addEventListener('show.bs.modal', () => {
        if (!editandoId) {
            modalTitle.textContent = 'Novo Medicamento';
            formMedicamento.reset();
            modalAlertPlaceholder.innerHTML = '';
        }
    });

    modalElement.addEventListener('hidden.bs.modal', () => {
        editandoId = null;
    });

    // Lida com o filtro de status
    filtroStatusSelect.addEventListener('change', () => carregarMedicamentos());

    // Delegação de eventos: ouve cliques na tabela inteira
    tabelaCorpo.addEventListener('click', (event) => {
        const target = event.target;

        if (target.classList.contains('btn-editar')) {
            editandoId = target.dataset.id;
            const nome = target.dataset.nome;
            const descricaoDetalhada = target.dataset.descricaoDetalhada;
            const unidadeMedida = target.dataset.unidadeMedida;
            modalTitle.textContent = `Editando Medicamento #${editandoId}`;
            nomeMedicamentoInput.value = nome;
            descricaoDetalhadaInput.value = descricaoDetalhada
            unidadeMedidaInput.value = unidadeMedida
            modalAlertPlaceholder.innerHTML = '';
            medicamentoModal.show();
        }

        if (target.classList.contains('btn-inativar')) {
            const id = target.dataset.id;
            if (confirm(`Tem a certeza que deseja inativar o medicamento ID #${id}?`)) {
                inativarMedicamento(id);
            }
        }

        if (target.classList.contains('btn-ativar')) {
            const id = target.dataset.id;
            if (confirm(`Tem a certeza que deseja ativar o medicamento ID #${id}?`)) {
                ativarMedicamento(id);
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
        const descricaoDetalhada = descricaoDetalhadaInput.value;
        const unidadeMedida = unidadeMedidaInput.value;

        const url = editandoId ? `/medicamentos/${editandoId}` : '/medicamentos';
        const method = editandoId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome , descricaoDetalhada, unidadeMedida }),
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