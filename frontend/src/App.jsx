import { useEffect, useState } from 'react'
import './App.css'

const API_URL = 'http://localhost:8080/api/v1/products'

const emptyForm = {
  name: '',
  description: '',
  price: '',
  quantity: '',
  category: '',
}

function App() {
  const [products, setProducts] = useState([])
  const [stats, setStats] = useState({ totalProducts: 0, activeProducts: 0 })
  const [form, setForm] = useState(emptyForm)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  const loadProducts = async () => {
    try {
      const res = await fetch(API_URL)
      if (!res.ok) throw new Error('Unable to load products')
      const data = await res.json()
      setProducts(data)
    } catch (err) {
      setError(err.message)
    }
  }

  const loadStats = async () => {
    try {
      const res = await fetch(`${API_URL}/stats/count`)
      if (!res.ok) throw new Error('Unable to load product stats')
      const data = await res.json()
      setStats(data)
    } catch (err) {
      console.error(err)
    }
  }

  const refreshData = async () => {
    setLoading(true)
    try {
      await Promise.all([loadProducts(), loadStats()])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    refreshData()
  }, [])

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSaving(true)
    setError('')

    try {
      const payload = {
        ...form,
        price: Number(form.price),
        quantity: Number(form.quantity),
      }

      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Failed to create product')
      }

      setForm(emptyForm)
      await refreshData()
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' })
      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Failed to delete product')
      }
      await refreshData()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">BuyEase commerce dashboard</p>
          <h1>Products</h1>
        </div>
        <div className="stats-grid">
          <div className="stat-card">
            <span>Total</span>
            <strong>{stats.totalProducts}</strong>
          </div>
          <div className="stat-card">
            <span>Active</span>
            <strong>{stats.activeProducts}</strong>
          </div>
        </div>
      </header>

      <main className="content-grid">
        <section className="panel form-panel">
          <h2>Add product</h2>
          <form onSubmit={handleSubmit} className="product-form">
            <label>
              Name
              <input name="name" value={form.name} onChange={handleChange} required />
            </label>
            <label>
              Description
              <textarea
                name="description"
                value={form.description}
                onChange={handleChange}
                rows="3"
              />
            </label>
            <div className="field-row">
              <label>
                Price
                <input
                  name="price"
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={form.price}
                  onChange={handleChange}
                  required
                />
              </label>
              <label>
                Quantity
                <input
                  name="quantity"
                  type="number"
                  min="0"
                  step="1"
                  value={form.quantity}
                  onChange={handleChange}
                  required
                />
              </label>
            </div>
            <label>
              Category
              <input name="category" value={form.category} onChange={handleChange} />
            </label>
            {error && <p className="error-message">{error}</p>}
            <button type="submit" disabled={saving}>
              {saving ? 'Saving...' : 'Create product'}
            </button>
          </form>
        </section>

        <section className="panel list-panel">
          <div className="list-header">
            <h2>Inventory</h2>
            <button type="button" className="secondary" onClick={refreshData}>
              Refresh
            </button>
          </div>

          {loading ? (
            <p>Loading products...</p>
          ) : products.length === 0 ? (
            <p className="empty-state">No products found.</p>
          ) : (
            <div className="product-list">
              {products.map((product) => (
                <article key={product.id} className="product-card">
                  <div className="product-topline">
                    <div>
                      <h3>{product.name}</h3>
                      <span className="category-tag">{product.category || 'Uncategorized'}</span>
                    </div>
                    <button
                      type="button"
                      className="delete-btn"
                      onClick={() => handleDelete(product.id)}
                    >
                      Delete
                    </button>
                  </div>
                  <p>{product.description || 'No description provided.'}</p>
                  <div className="meta-row">
                    <span>Price: ${Number(product.price).toFixed(2)}</span>
                    <span>Qty: {product.quantity}</span>
                    <span>{product.isActive ? 'Active' : 'Inactive'}</span>
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  )
}

export default App
