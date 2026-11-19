import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { autorService } from '../services/api';

function AutorForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editando = Boolean(id);

  const [autor, setAutor] = useState({ nome: '', nacionalidade: '', dataNascimento: '' });
  const [erro, setErro] = useState('');

  useEffect(() => {
    if (editando) {
      autorService
        .buscar(id)
        .then((res) =>
          setAutor({
            nome: res.data.nome || '',
            nacionalidade: res.data.nacionalidade || '',
            dataNascimento: res.data.dataNascimento || '',
          })
        )
        .catch(() => setErro('Autor não encontrado.'));
    }
  }, [id, editando]);

  const alterar = (e) => setAutor({ ...autor, [e.target.name]: e.target.value });

  const salvar = (e) => {
    e.preventDefault();
    const payload = {
      nome: autor.nome,
      nacionalidade: autor.nacionalidade || null,
      dataNascimento: autor.dataNascimento || null,
    };
    const requisicao = editando ? autorService.atualizar(id, payload) : autorService.criar(payload);
    requisicao
      .then(() => navigate('/autores'))
      .catch(() => setErro('Erro ao salvar o autor. Verifique os campos.'));
  };

  return (
    <div>
      <h2>{editando ? 'Editar autor' : 'Novo autor'}</h2>
      {erro && <div className="erro">{erro}</div>}

      <form onSubmit={salvar}>
        <label htmlFor="nome">Nome *</label>
        <input id="nome" name="nome" value={autor.nome} onChange={alterar} maxLength={100} required />

        <label htmlFor="nacionalidade">Nacionalidade</label>
        <input
          id="nacionalidade"
          name="nacionalidade"
          value={autor.nacionalidade}
          onChange={alterar}
          maxLength={50}
        />

        <label htmlFor="dataNascimento">Data de nascimento</label>
        <input
          id="dataNascimento"
          name="dataNascimento"
          type="date"
          value={autor.dataNascimento}
          onChange={alterar}
        />

        <div className="acoes">
          <button type="submit" className="btn">Salvar</button>
          <Link to="/autores" className="btn secondary">Cancelar</Link>
        </div>
      </form>
    </div>
  );
}

export default AutorForm;
