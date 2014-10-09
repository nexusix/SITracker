/*
 * Copyright 2014 Gleb Godonoga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrada.sitracker.events;

import java.util.Date;

public class RatingResultEvent {

    public final boolean ratingSubmissionResult;
    public final int ratingValue;
    private final Date ratingTime = new Date();
    public final String voteCookie;

    public RatingResultEvent(boolean result, int ratingValue, String voteCookie) {
        ratingSubmissionResult = result;
        this.ratingValue = ratingValue;
        this.voteCookie = voteCookie;
    }

    public Date getRatingTime() {
        return (Date) ratingTime.clone();
    }
}