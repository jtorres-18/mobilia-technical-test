function StatusBadge({ status }) {
    const isActive = status === 'ACTIVE'

    return (
        <span
            className={`status-badge ${
                isActive
                    ? 'status-badge--active'
                    : 'status-badge--inactive'
            }`}
        >
      {isActive ? 'Activo' : 'Inactivo'}
    </span>
    )
}

export default StatusBadge