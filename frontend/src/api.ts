import type { CurrentUser, Review } from './types'

const API_URL = import.meta.env.VITE_API_URL

const reviewUrl = (id: number) => `${API_URL}/api/reviews/${id}`

async function readJson<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const body = await response.json().catch(() => null) as { message?: string } | null
    throw new Error(body?.message ?? `Request failed with status ${response.status}`)
  }
  return response.json() as Promise<T>
}

export async function submitReview(prUrl: string): Promise<Review> {
  const response = await fetch(`${API_URL}/api/reviews`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ prUrl }),
  })
  return readJson<Review>(response)
}

export async function getReview(id: number): Promise<Review> {
  return readJson<Review>(await fetch(reviewUrl(id), { credentials: 'include' }))
}

export async function retryReview(id: number): Promise<Review> {
  return readJson<Review>(await fetch(`${reviewUrl(id)}/retry`, { method: 'POST', credentials: 'include' }))
}

export async function getCurrentUser(): Promise<CurrentUser | null> {
  const response = await fetch(`${API_URL}/api/me`, { credentials: 'include' })
  if (response.status === 401 || response.status === 403) return null
  return readJson<CurrentUser>(response)
}