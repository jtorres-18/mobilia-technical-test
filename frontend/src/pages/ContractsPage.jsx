import { useEffect, useState } from 'react'
import {
    getContracts,
    searchContracts,
} from '../services/contractService'
import ContractTable from '../components/ContractTable'
import SearchBar from '../components/SearchBar'
import '../styles/contracts.css'

function ContractsPage() {
    const [contracts, setContracts] = useState([])
    const [searchTerm, setSearchTerm] = useState('')
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [emptyMessage, setEmptyMessage] = useState('')

    const loadContracts = async () => {
        setLoading(true)
        setError('')
        setEmptyMessage('')

        try {
            const data = await getContracts()
            setContracts(data)

            if (data.length === 0) {
                setEmptyMessage('No hay contratos registrados.')
            }
        } catch (err) {
            console.error(err)
            setError(
                'No fue posible cargar los contratos. Intenta nuevamente.'
            )
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        loadContracts()
    }, [])

    const handleSearch = async () => {
        const term = searchTerm.trim()

        if (!term) {
            await loadContracts()
            return
        }

        setLoading(true)
        setError('')
        setEmptyMessage('')

        try {
            const data = await searchContracts(term)
            setContracts(data)
        } catch (err) {
            if (err.response?.status === 404) {
                setContracts([])
                setEmptyMessage(
                    `No se encontraron contratos para "${term}".`
                )
            } else {
                console.error(err)
                setError(
                    'Ocurrió un error al buscar los contratos.'
                )
            }
        } finally {
            setLoading(false)
        }
    }

    const handleClear = async () => {
        setSearchTerm('')
        await loadContracts()
    }

    return (
        <main className="contracts-page">
            <div className="contracts-content">
                <header className="page-header">
                    <div>
                        <p className="page-eyebrow">
                            Gestión inmobiliaria
                        </p>

                        <h1>Historial de contratos</h1>

                        <p className="page-description">
                            Consulta los contratos asociados a los inmuebles
                            y sus participantes.
                        </p>
                    </div>

                    {!loading && !error && (
                        <div className="contracts-counter">
                            <strong>{contracts.length}</strong>
                            <span>
                {contracts.length === 1
                    ? 'contrato'
                    : 'contratos'}
              </span>
                        </div>
                    )}
                </header>

                <section className="search-section">
                    <SearchBar
                        value={searchTerm}
                        onChange={setSearchTerm}
                        onSearch={handleSearch}
                        onClear={handleClear}
                        loading={loading}
                    />
                </section>

                <section className="contracts-section">
                    {loading && (
                        <div className="state-message">
                            <div className="loader" />
                            <p>Cargando contratos...</p>
                        </div>
                    )}

                    {!loading && error && (
                        <div className="state-message state-message--error">
                            <h2>No fue posible completar la operación</h2>
                            <p>{error}</p>

                            <button
                                type="button"
                                className="secondary-button"
                                onClick={loadContracts}
                            >
                                Reintentar
                            </button>
                        </div>
                    )}

                    {!loading && !error && contracts.length === 0 && (
                        <div className="state-message">
                            <h2>No hay resultados</h2>
                            <p>{emptyMessage}</p>
                        </div>
                    )}

                    {!loading && !error && contracts.length > 0 && (
                        <ContractTable contracts={contracts} />
                    )}
                </section>
            </div>
        </main>
    )
}

export default ContractsPage