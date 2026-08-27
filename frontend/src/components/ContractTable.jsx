import StatusBadge from './StatusBadge'

const PROPERTY_TYPE_LABELS = {
    HOUSE: 'Casa',
    APARTMENT: 'Apartamento',
    COMMERCIAL_SPACE: 'Local',
}

function PeopleList({ people }) {
    if (!people || people.length === 0) {
        return <span className="empty-value">Sin fiador</span>
    }

    return (
        <div className="people-list">
            {people.map((person) => (
                <span key={person} className="person-chip">
          {person}
        </span>
            ))}
        </div>
    )
}

function ContractTable({ contracts }) {
    return (
        <div className="table-container">
            <table className="contracts-table">
                <thead>
                <tr>
                    <th>Contrato</th>
                    <th>Estado</th>
                    <th>Direccion</th>
                    <th>Tipo</th>
                    <th>Arrendatario</th>
                    <th>Propietarios</th>
                    <th>Deudor</th>
                </tr>
                </thead>

                <tbody>
                {contracts.map((contract) => (
                    <tr key={contract.contractCode}>
                        <td>
                            <strong>{contract.contractCode}</strong>
                        </td>

                        <td>
                            <StatusBadge status={contract.status} />
                        </td>

                        <td>
                            {contract.propertyAddress}
                        </td>

                        <td>
                            {PROPERTY_TYPE_LABELS[contract.propertyType]
                                ?? contract.propertyType}
                        </td>

                        <td>
                            {contract.tenant}
                        </td>

                        <td>
                            <PeopleList people={contract.owners} />
                        </td>

                        <td>
                            <PeopleList people={contract.guarantors} />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default ContractTable