import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  private readonly PREFIX = 'drafts/v2';
  private readonly SEPARATOR = '::';

  // --------------------------
  // Public API
  // --------------------------

  saveDraft(userId: number, draft: any): void {
    this.validateUserId(userId);
    const key = this.generateKey(userId);
    const current = this.getAllDrafts(userId);
    const updated = this.upsertDraft(current, draft);
    this.safeSetItem(key, updated);
  }

  deleteDraft(userId: number, draftId: number): void {
    const drafts = this.getAllDrafts(userId).filter((d) => d.id !== draftId);
    this.safeSetItem(this.generateKey(userId), drafts);
  }

  getAllDrafts(userId: number): any[] {
    return this.safeGetItem(this.generateKey(userId)) || [];
  }

  // --------------------------
  // Private Methods
  // --------------------------

  private generateKey(userId: number): string {
    return `${this.PREFIX}${this.SEPARATOR}${userId}`;
  }

  private upsertDraft(drafts: any[], newDraft: any): any[] {
    const index = drafts.findIndex((d) => d.id === newDraft.id);
    if (index >= 0) {
      drafts[index] = newDraft; // Update existing
    } else {
      drafts.push(newDraft); // Add new
    }
    return drafts;
  }

  private safeSetItem(key: string, value: any): void {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('StorageService: Failed to save', error);
      throw new Error('STORAGE_SAVE_FAILED');
    }
  }

  private safeGetItem(key: string): any {
    try {
      const data = localStorage.getItem(key);
      return data ? JSON.parse(data) : null;
    } catch (error) {
      console.error('StorageService: Failed to read', error);
      return null;
    }
  }

  private validateUserId(userId: number): void {
    if (!Number.isInteger(userId)) {
      throw new Error(`Invalid userId: ${userId}`);
    }
  }
}
