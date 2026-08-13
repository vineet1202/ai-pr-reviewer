import { useEffect, useState } from 'react'
import { AlertCircle, ArrowUpRight, CheckCircle2, Code2, Github, LoaderCircle, RefreshCw, Send, ShieldCheck, Sparkles } from 'lucide-react'
import { getCurrentUser, getReview, retryReview, submitReview } from './api'
import type { CurrentUser, Review, ReviewIssue, ReviewStatus } from './types'

const statusClass: Record<ReviewStatus, string> = {
  PENDING: 'bg-amber-50 text-amber-700 ring-amber-200',
  IN_PROGRESS: 'bg-blue-50 text-blue-700 ring-blue-200',
  COMPLETED: 'bg-emerald-50 text-emerald-700 ring-emerald-200',
  FAILED: 'bg-rose-50 text-rose-700 ring-rose-200',
}

const severityClass: Record<ReviewIssue['severity'], string> = {
  HIGH: 'bg-rose-50 text-rose-700',
  MEDIUM: 'bg-amber-50 text-amber-700',
  LOW: 'bg-slate-100 text-slate-600',
}

function formatDate(value: string | null) {
  return value ? new Intl.DateTimeFormat(undefined, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(value)) : '—'
}

function StatusPill({ status }: { status: ReviewStatus }) {
  return <span className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-xs font-semibold ring-1 ring-inset ${statusClass[status]}`}>
    {(status === 'PENDING' || status === 'IN_PROGRESS') && <LoaderCircle className="size-3 animate-spin" />}
    {status.replace('_', ' ')}
  </span>
}

function IssueCard({ issue }: { issue: ReviewIssue }) {
  return <article className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
    <div className="flex flex-wrap items-center gap-2">
      <span className={`rounded-full px-2.5 py-1 text-[11px] font-bold tracking-wide ${severityClass[issue.severity]}`}>{issue.severity}</span>
      <span className="text-xs font-medium text-slate-500">{issue.category}</span>
      <span className="ml-auto text-xs text-slate-400">{issue.filePath}{issue.diffPosition ? ` · diff line ${issue.diffPosition}` : ''}</span>
    </div>
    <p className="mt-3 text-sm leading-6 text-slate-700">{issue.message}</p>
  </article>
}

export default function App() {
  const [prUrl, setPrUrl] = useState('')
  const [review, setReview] = useState<Review | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null)
  const [isLoadingUser, setIsLoadingUser] = useState(true)

  useEffect(() => {
    getCurrentUser()
      .then(setCurrentUser)
      .catch(() => setCurrentUser(null))
      .finally(() => setIsLoadingUser(false))
  }, [])

  useEffect(() => {
    if (!review || !['PENDING', 'IN_PROGRESS'].includes(review.status)) return
    const timeout = window.setTimeout(async () => {
      try { setReview(await getReview(review.id)) }
      catch (requestError) { setError(requestError instanceof Error ? requestError.message : 'Could not refresh review status.') }
    }, 2500)
    return () => window.clearTimeout(timeout)
  }, [review])

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (!currentUser) return
    setError(null); setIsSubmitting(true)
    try { setReview(await submitReview(prUrl)); setPrUrl('') }
    catch (requestError) { setError(requestError instanceof Error ? requestError.message : 'Could not submit the review.') }
    finally { setIsSubmitting(false) }
  }

  async function handleRetry() {
    if (!review) return
    setError(null)
    try { setReview(await retryReview(review.id)) }
    catch (requestError) { setError(requestError instanceof Error ? requestError.message : 'Could not retry the review.') }
  }

  return <main className="min-h-screen bg-[radial-gradient(circle_at_top_right,_#e0e7ff_0,_transparent_28rem)]">
    <div className="mx-auto max-w-5xl px-5 py-7 sm:px-8 sm:py-10">
      <header className="flex items-center justify-between">
        <a className="flex items-center gap-2 font-bold tracking-tight text-slate-900" href="/">
          <span className="grid size-9 place-items-center rounded-xl bg-indigo-600 text-white shadow-lg shadow-indigo-200"><Code2 className="size-5" /></span>
          Review.ai
        </a>
        {isLoadingUser ? <span className="size-8 animate-spin rounded-full border-2 border-slate-200 border-t-indigo-600" /> : currentUser ? <div className="flex items-center gap-2 rounded-lg bg-white px-3 py-2 text-sm font-semibold text-slate-700 shadow-sm ring-1 ring-slate-200"><span className="grid size-6 place-items-center overflow-hidden rounded-full bg-indigo-100 text-xs text-indigo-700">{currentUser.avatarUrl ? <img src={currentUser.avatarUrl} alt="" className="size-full object-cover" /> : currentUser.name.slice(0, 1).toUpperCase()}</span>{currentUser.name}</div> : <a className="inline-flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium text-slate-600 transition hover:bg-white hover:text-slate-900" href="http://localhost:8080/oauth2/authorization/github"><Github className="size-4" /> Sign in with GitHub</a>}
      </header>

      <section className="mx-auto max-w-3xl pb-12 pt-18 text-center sm:pb-16 sm:pt-24">
        <div className="mb-5 inline-flex items-center gap-2 rounded-full border border-indigo-100 bg-indigo-50 px-3 py-1.5 text-xs font-semibold text-indigo-700"><Sparkles className="size-3.5" /> AI-assisted pull request review</div>
        <h1 className="text-balance text-4xl font-bold tracking-tight text-slate-900 sm:text-5xl">A faster first pass on every pull request.</h1>
        <p className="mx-auto mt-5 max-w-2xl text-pretty text-base leading-7 text-slate-600 sm:text-lg">Connect GitHub, paste a pull request URL, and get focused findings from the changed code. Your review stays visible while it runs.</p>
      </section>

      <section className="mx-auto max-w-3xl rounded-2xl border border-slate-200 bg-white p-4 shadow-xl shadow-slate-200/50 sm:p-6">
        {isLoadingUser ? <div className="flex items-center justify-center gap-3 py-6 text-sm text-slate-500"><LoaderCircle className="size-5 animate-spin" /> Checking your GitHub session...</div> : !currentUser ? <div className="flex flex-col items-start gap-4 rounded-xl bg-slate-50 p-5 sm:flex-row sm:items-center sm:justify-between"><div><h2 className="font-semibold text-slate-900">Sign in to start a review</h2><p className="mt-1 text-sm leading-6 text-slate-600">Your GitHub account is used to securely read pull requests you can access.</p></div><a href="http://localhost:8080/oauth2/authorization/github" className="inline-flex shrink-0 items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-indigo-700"><Github className="size-4" /> Sign in with GitHub</a></div> : <form onSubmit={handleSubmit} className="flex flex-col gap-3 sm:flex-row">
          <label className="sr-only" htmlFor="pr-url">GitHub pull request URL</label>
          <input id="pr-url" value={prUrl} onChange={(event) => setPrUrl(event.target.value)} required type="url" placeholder="https://github.com/owner/repository/pull/42" className="min-w-0 flex-1 rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:bg-white focus:ring-4 focus:ring-indigo-100" />
          <button disabled={isSubmitting} className="inline-flex items-center justify-center gap-2 rounded-xl bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60">
            {isSubmitting ? <LoaderCircle className="size-4 animate-spin" /> : <Send className="size-4" />} Review PR
          </button>
        </form>}
        <div className="mt-4 flex flex-wrap gap-x-5 gap-y-2 text-xs text-slate-500"><span className="inline-flex items-center gap-1.5"><ShieldCheck className="size-3.5 text-emerald-600" /> GitHub OAuth protected</span><span>Analysis runs in the background</span><span>Diff-only findings</span></div>
      </section>

      {error && <div className="mx-auto mt-6 flex max-w-3xl gap-3 rounded-xl border border-rose-200 bg-rose-50 p-4 text-sm text-rose-800"><AlertCircle className="mt-0.5 size-5 shrink-0" /><div><p className="font-semibold">Something went wrong</p><p className="mt-1 text-rose-700">{error}</p></div></div>}

      {review && <section className="mx-auto mt-10 max-w-3xl">
        <div className="rounded-2xl border border-slate-200 bg-white shadow-sm">
          <div className="flex flex-col gap-4 border-b border-slate-100 p-5 sm:flex-row sm:items-start sm:justify-between sm:p-6">
            <div>
              <div className="flex items-center gap-2"><StatusPill status={review.status} /><span className="text-xs text-slate-400">Review #{review.id}</span></div>
              <a className="mt-3 inline-flex items-center gap-1 text-base font-semibold text-slate-900 hover:text-indigo-600" href={review.prUrl} target="_blank" rel="noreferrer">{review.repoOwner}/{review.repoName} <ArrowUpRight className="size-4" /></a>
              <p className="mt-1 text-sm text-slate-500">Pull request #{review.prNumber} · Submitted {formatDate(review.createdAt)}</p>
            </div>
            {review.status === 'FAILED' && <button onClick={handleRetry} className="inline-flex items-center justify-center gap-2 rounded-lg border border-slate-200 px-3.5 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50"><RefreshCw className="size-4" /> Retry review</button>}
          </div>
          <div className="p-5 sm:p-6">
            {(review.status === 'PENDING' || review.status === 'IN_PROGRESS') && <div className="flex items-start gap-3 rounded-xl bg-blue-50 p-4 text-sm text-blue-800"><LoaderCircle className="mt-0.5 size-5 animate-spin" /><div><p className="font-semibold">Review in progress</p><p className="mt-1 text-blue-700">Fetching the changed files and checking them with the AI. This page refreshes automatically.</p></div></div>}
            {review.status === 'FAILED' && <div className="flex items-start gap-3 rounded-xl bg-rose-50 p-4 text-sm text-rose-800"><AlertCircle className="mt-0.5 size-5 shrink-0" /><div><p className="font-semibold">The review could not be completed</p><p className="mt-1 text-rose-700">{review.failureReason ?? 'No failure reason was returned.'}</p></div></div>}
            {review.status === 'COMPLETED' && <><div className="mb-5 flex items-center justify-between"><div><h2 className="text-lg font-bold text-slate-900">Review findings</h2><p className="mt-1 text-sm text-slate-500">Completed {formatDate(review.completedAt)}</p></div><span className="inline-flex items-center gap-1.5 text-sm font-semibold text-emerald-700"><CheckCircle2 className="size-4" /> {review.issues.length} found</span></div>{review.issues.length === 0 ? <p className="rounded-xl bg-emerald-50 p-4 text-sm text-emerald-800">No issues were identified in this diff.</p> : <div className="space-y-3">{review.issues.map((issue, index) => <IssueCard issue={issue} key={`${issue.filePath}-${index}`} />)}</div>}</>}
          </div>
        </div>
      </section>}
    </div>
  </main>
}
