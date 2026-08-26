function SearchBar({
                       value,
                       onChange,
                       onSearch,
                       onClear,
                       loading,
                   }) {
    const handleSubmit = (event) => {
        event.preventDefault()
        onSearch()
    }

    return (
        <form className="search-bar" onSubmit={handleSubmit}>
            <div className="search-field">
                <label htmlFor="contract-search">
                    Buscar contratos
                </label>

                <div className="search-controls">
                    <input
                        id="contract-search"
                        type="text"
                        value={value}
                        onChange={(event) => onChange(event.target.value)}
                        placeholder="Nombre, apellido, documento, correo, dirección o código"
                        autoComplete="off"
                    />

                    <button
                        type="submit"
                        className="primary-button"
                        disabled={loading}
                    >
                        {loading ? 'Buscando...' : 'Buscar'}
                    </button>

                    {value && (
                        <button
                            type="button"
                            className="secondary-button"
                            onClick={onClear}
                            disabled={loading}
                        >
                            Limpiar
                        </button>
                    )}
                </div>
            </div>
        </form>
    )
}

export default SearchBar