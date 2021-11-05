package org.sheedon.upgradedispatcher.download;

import androidx.annotation.NonNull;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;

import java.io.File;

/**
 * 下载管理器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/25 4:02 下午
 */
class DownloadManager {
    private static DownloadManager instance;

    private DownloadTask task;

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    private DownloadManager() {

    }

    public void downloadSingleTask(String url, File parentFile, String fileName, DownloadListener listener) {

        task = new DownloadTask.Builder(url, parentFile)
                .setFilename(fileName)
                .setMinIntervalMillisCallbackProcess(30)
                .setPassIfAlreadyCompleted(true)
                .build();

        task.enqueue(new SingleDownloadListener(listener));
    }

    public void cancel() {
        task.cancel();
    }

    public void destroy() {
        instance = null;
    }


    // 单一任务下载监听器
    private static class SingleDownloadListener extends DownloadListener3 {

        private final DownloadListener listener;

        SingleDownloadListener(DownloadListener listener) {
            this.listener = listener;
        }

        @Override
        protected void started(@NonNull DownloadTask task) {
            if (listener != null) {
                listener.start(task);
            }
        }

        @Override
        public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

        }

        @Override
        public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

        }

        @Override
        public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
            if (listener != null) {
                long progress = currentOffset * 100 / totalLength;
                listener.progress((int) progress);
            }
        }


        @Override
        protected void completed(@NonNull DownloadTask task) {
            if (listener != null) {
                listener.completed();
            }
        }

        @Override
        protected void canceled(@NonNull DownloadTask task) {

        }

        @Override
        protected void error(@NonNull DownloadTask task, @NonNull Exception e) {
            if (listener != null) {
                listener.error(e.getMessage());
            }
        }

        @Override
        protected void warn(@NonNull DownloadTask task) {
            if (listener != null) {
                listener.error("DownloadTask warn");
            }
        }

    }
}
