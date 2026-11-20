import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { autorService, livroService } from '../services/api';

function LivroForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const editando = Boolean(id);

  const [livro, setLivro] = useState({
    titulo: '',
    isbn: '',
    anoPublicacao: '',
    genero: '',
    autorId: '',
  });
  const [autores, setAutores] = useState([]);
  const [erro, setErro] = useState('');

  useEffect(() => {
    autorService
      .listar()
      .then((res) => setAutores(res.data))
      .catch(() => setErro('Erro ao carregar os autores.'));
  }, []);

  useEffect(() => {
    if (editando) {
      livroService
        .buscar(id)
        .then((res) =>
          setLivro({
            titulo: res.data.titulo || '',
            isbn: res.data.isbn || '',
            anoPublicacao: res.data.anoPublicacao || '',
            genero: res.data.genero || '',
            autorId: res.data.autor ? String(res.data.autor.id) : '',
          })
        )
        .catch(() => setErro('Livro não encontrado.'));
    }
  }, [id, editando]);

  const alterar = (e) => setLivro({ ...livro, [e.target.name]: e.target.value });

  const salvar = (e) => {
    e.preventDefault();
    if (!livro.autorId) {
      setErro('Selecione um autor.');
      return;
    }
    // o autor vai somente com o id: "autor": {"id": 1}
    const payload = {
      titulo: livro.titulo,
      isbn: livro.isbn || null,
      anoPublicacao: livro.anoPublicacao ? parseInt(livro.anoPublicacao, 10) : null,
      genero: livro.genero || null,
      autor: { id: parseInt(livro.autorId, 10) },
    };
    const requisicao = editando ? livroService.atualizar(id, payload) : livroService.criar(payload);
    requisicao
      .then(() => navigate('/livros'))
      .catch(() => setErro('Erro ao salvar o livro. Verifique os campos.'));
  };

  return (
    <div>
      <h2>{editando ? 'Editar livro' : 'Novo livro'}</h2>
      {erro && <div className="erro">{erro}</div>}

      <form onSubmit={salvar}>
        <label htmlFor="titulo">Título *</label>
        <input id="titulo" name="titulo" value={livro.titulo} onChange={alterar} maxLength={200} required />

        <label htmlFor="autorId">Autor *</label>
        <select id="autorId" name="autorId" value={livro.autorId} onChange={alterar} required>
          <option value="">Selecione um autor</option>
          {autores.map((a) => (
            <option key={a.id} value={a.id}>
              {a.nome}
            </option>
          ))}
        </select>

        <label htmlFor="isbn">ISBN</label>
        <input id="isbn" name="isbn" value={livro.isbn} onChange={alterar} maxLength={20} />

        <label htmlFor="anoPublicacao">Ano de publicação</label>
        <input
          id="anoPublicacao"
          name="anoPublicacao"
          type="number"
          value={livro.anoPublicacao}
          onChange={alterar}
        />

        <label htmlFor="genero">Gênero</label>
        <input id="genero" name="genero" value={livro.genero} onChange={alterar} maxLength={50} />

        <div className="acoes">
          <button type="submit" className="btn">Salvar</button>
          <Link to="/livros" className="btn secondary">Cancelar</Link>
        </div>
      </form>
    </div>
  );
}

export default LivroForm;
