export type ReviewStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'

export type ReviewIssue = {
  filePath: string
  diffPosition: number | null
  severity: 'HIGH' | 'MEDIUM' | 'LOW'
  category: 'BUG' | 'SECURITY' | 'STYLE' | 'PERFORMANCE' | 'MAINTAINABILITY'
  message: string
}

export type Review = {
  id: number
  prUrl: string
  repoOwner: string
  repoName: string
  prNumber: number
  status: ReviewStatus
  failureReason: string | null
  createdAt: string
  completedAt: string | null
  issues: ReviewIssue[]
}

export type CurrentUser = {
  name: string
  avatarUrl: string | null
}
